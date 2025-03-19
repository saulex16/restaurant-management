package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Kitchen;
import at.technikum_wien.restaurant_management.model.notifications.*;
import at.technikum_wien.restaurant_management.model.notifications.Observer;
import at.technikum_wien.restaurant_management.model.orders.Order;
import at.technikum_wien.restaurant_management.model.orders.OrderState;
import at.technikum_wien.restaurant_management.repository.interfaces.KitchenRepository;
import at.technikum_wien.restaurant_management.repository.interfaces.OrderRepository;
import at.technikum_wien.restaurant_management.service.interfaces.KitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class KitchenServiceImpl implements KitchenService, Observer<Order> {

    private final Map<Kitchen, Queue<Order>> ordersToCookByKitchen;
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
    }

    @Override
    public void notify(Notification<Order> notification) {
        Order order = notification.getPayload();
        Kitchen kitchen = order.getRestaurant().getKitchen();
        ordersToCookByKitchen.putIfAbsent(kitchen, new LinkedList<>());
        ordersToCookByKitchen.get(kitchen).add(order);
    }

    @Override
    public List<NotificationType> getNotificationTypes() {
        return List.of(NotificationType.NEW_ORDER);
    }

    @Override
    public Order getNextOrder(long kitchenId){
        Optional<Kitchen> maybeKitchen = kitchenRepository.findById(kitchenId);
        if (maybeKitchen.isEmpty()) {
            throw new IllegalArgumentException("Kitchen not found");
        }
        Kitchen kitchen = maybeKitchen.get();
        Order order = ordersToCookByKitchen.getOrDefault(kitchen, new LinkedList<>()).poll();
        if (order == null) {
            throw new IllegalArgumentException("No orders to cook");
        }
        return order;
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

        notifier.notify(orderReadyNotificationFactory.createNotification(order));
    }


}
