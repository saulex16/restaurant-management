package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Kitchen;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;
import at.technikum_wien.restaurant_management.model.notifications.*;
import at.technikum_wien.restaurant_management.model.notifications.Observer;
import at.technikum_wien.restaurant_management.model.orders.Order;
import at.technikum_wien.restaurant_management.model.orders.OrderState;
import at.technikum_wien.restaurant_management.repository.interfaces.KitchenRepository;
import at.technikum_wien.restaurant_management.repository.interfaces.OrderRepository;
import at.technikum_wien.restaurant_management.service.interfaces.KitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class KitchenServiceImpl implements KitchenService, Observer<Order> {

    private final Map<Kitchen, Queue<Order>> ordersToCookByKitchen;
    private final ConcurrentHashMap<Long, ThreadPoolTaskScheduler> kitchenSchedulers = new ConcurrentHashMap<>();
    private final KitchenRepository kitchenRepository;
    private final OrderRepository orderRepository;
    private final Notifier notifier;
    private final OrderReadyNotificationFactory orderReadyNotificationFactory;

    @Autowired
    public KitchenServiceImpl(KitchenRepository kitchenRepository, OrderRepository orderRepository, Notifier notifier, OrderReadyNotificationFactory orderReadyNotificationFactory) {
        ordersToCookByKitchen = new HashMap<>();
        this.kitchenRepository = kitchenRepository;
        this.orderRepository = orderRepository;
        this.notifier = notifier;
        this.orderReadyNotificationFactory = orderReadyNotificationFactory;

        List<NotificationType> notificationTypes = this.getNotificationTypes();
        for (NotificationType type: notificationTypes){
            notifier.subscribe(type, this);
        }
    }

    @Override
    public void notify(Notification<Order> notification) {
        Order order = notification.getPayload();
        Kitchen kitchen = order.getRestaurant().getKitchen();
        ordersToCookByKitchen.putIfAbsent(kitchen, new LinkedList<>());
        ordersToCookByKitchen.get(kitchen).add(order);

        getNextOrder(kitchen.getId());
    }

    @Override
    public List<NotificationType> getNotificationTypes() {
        return List.of(NotificationType.NEW_ORDER);
    }

    private ThreadPoolTaskScheduler getSchedulerForKitchen(Long kitchenId){
        return kitchenSchedulers.computeIfAbsent(kitchenId, id -> {
            Kitchen kitchen = kitchenRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Kitchen not found"));

            ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
            scheduler.setPoolSize(kitchen.getDishesLimit());
            scheduler.setThreadNamePrefix("Kitchen-" + kitchenId + "-Task-");
            scheduler.initialize();

            return scheduler;
        });
    }

    @Override
    public void getNextOrder(long kitchenId){
        Optional<Kitchen> maybeKitchen = kitchenRepository.findById(kitchenId);
        if (maybeKitchen.isEmpty()) {
            throw new IllegalArgumentException("Kitchen not found");
        }
        Kitchen kitchen = maybeKitchen.get();

        if(kitchen.getCurrentDishes() == kitchen.getDishesLimit()){
            return;
        }

        Order order = ordersToCookByKitchen.getOrDefault(kitchen, new LinkedList<>()).poll();
        if (order == null) {
            throw new IllegalArgumentException("No orders to cook");
        }

        kitchen.setCurrentDishes(kitchen.getCurrentDishes() + 1);
        kitchenRepository.save(kitchen);

        this.cookOrder(order.getId());
    }

    @Override
    public void cookOrder(long orderId){
        Optional<Order> maybeOrder = orderRepository.findById(orderId);
        if (maybeOrder.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        Order order = maybeOrder.get();
        if (order.getState() != OrderState.QUEUED) {
            throw new IllegalArgumentException("Order is not in the correct state");
        }
        order.setState(OrderState.PREPARING);
        orderRepository.save(order);

        TaskScheduler scheduler = getSchedulerForKitchen(order.getRestaurant().getKitchen().getId());
        scheduler.schedule(()-> setOrderReady(orderId), Instant.now().plusSeconds(
                order.getOrderedDishes().stream()
                .map(OrderedDish::getDish)
                .map(Dish::getDurationInMinutes)
                .reduce(Integer::max)
                        .get()));
    }

    @Override
    public void setOrderReady(long orderId){
        Optional<Order> maybeOrder = orderRepository.findById(orderId);
        if (maybeOrder.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        Order order = maybeOrder.get();
        if (order.getState() != OrderState.PREPARING) {
            throw new IllegalArgumentException("Order is not in the correct state");
        }
        order.setState(OrderState.COOKED);
        orderRepository.save(order);

        Kitchen kitchen = order.getRestaurant().getKitchen();
        kitchen.setCurrentDishes(kitchen.getCurrentDishes() - 1);
        kitchenRepository.save(kitchen);

        notifier.notify(orderReadyNotificationFactory.createNotification(order));
        getNextOrder(kitchen.getId());
    }


}
