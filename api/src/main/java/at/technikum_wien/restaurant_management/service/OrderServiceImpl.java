package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.bills.Bill;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.Waiter;
import at.technikum_wien.restaurant_management.model.bills.SimpleBillVisitor;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.dishes.OrderableDish;
import at.technikum_wien.restaurant_management.model.dishes.OrderableDishIngredientDecorator;
import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;
import at.technikum_wien.restaurant_management.model.notifications.*;
import at.technikum_wien.restaurant_management.model.orders.Order;
import at.technikum_wien.restaurant_management.model.orders.OrderState;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.model.tables.Table;
import at.technikum_wien.restaurant_management.repository.interfaces.*;
import at.technikum_wien.restaurant_management.service.interfaces.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService, Observer<Order> {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final TableRepository tableRepository;
    private final WaiterRepository waiterRepository;
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;
    private final OrderedDishRepository orderedDishRepository;
    private final Notifier notifier;
    private final NewOrderNotificationFactory newOrderNotificationFactory;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            RestaurantRepository restaurantRepository,
            TableRepository tableRepository,
            WaiterRepository waiterRepository,
            DishRepository dishRepository,
            IngredientRepository ingredientRepository,
            OrderedDishRepository orderedDishRepository,
            Notifier notifier,
            NewOrderNotificationFactory newOrderNotificationFactory
    ) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.tableRepository = tableRepository;
        this.waiterRepository = waiterRepository;
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
        this.orderedDishRepository = orderedDishRepository;
        this.notifier = notifier;
        this.newOrderNotificationFactory = newOrderNotificationFactory;

        List<NotificationType> notificationTypes = this.getNotificationTypes();
        for (NotificationType type: notificationTypes){
            notifier.subscribe(type, this);
        }
    }

    @Override
    public List<NotificationType> getNotificationTypes() {
        return List.of(NotificationType.ORDER_READY);
    }

    @Override
    public void notify(Notification<Order> notification) {
        Order order = notification.getPayload();
        NotificationType notificationType = notification.getNotificationType();
        if (!notificationType.equals(NotificationType.ORDER_READY)) {
            throw new IllegalArgumentException("Invalid notification type");
        }
        if (!order.getState().equals(OrderState.COOKED)) {
            throw new IllegalArgumentException("Invalid order state");
        }
        deliverOrder(order);
    }

    @Override
    public long createOrder(long restaurantId, long tableId, long waiterId) {
        Optional<Restaurant> maybeRestaurant = restaurantRepository.findById(restaurantId);
        if (maybeRestaurant.isEmpty()) {
            throw new IllegalArgumentException("Restaurant not found");
        }
        Optional<Table> maybeTable = tableRepository.findById(tableId);
        if (maybeTable.isEmpty()) {
            throw new IllegalArgumentException("Table not found");
        }
        Optional<Waiter> maybeWaiter = waiterRepository.findById(waiterId);
        if (maybeWaiter.isEmpty()) {
            throw new IllegalArgumentException("Waiter not found");
        }
        Restaurant restaurant = maybeRestaurant.get();
        Table table = maybeTable.get();
        Waiter waiter = maybeWaiter.get();
        if (!table.getRestaurant().getId().equals(restaurant.getId())) {
            throw new IllegalArgumentException("Table does not belong to restaurant");
        }
        if (table.isOccupied()) {
            throw new IllegalArgumentException("Table is occupied");
        }
        if (!waiter.getRestaurant().getId().equals(restaurant.getId())) {
            throw new IllegalArgumentException("Waiter does not belong to restaurant");
        }
        Order order = new Order(restaurant, table, waiter, OrderState.CREATED);
        orderRepository.save(order);

        table.setOrder(order);
        tableRepository.save(table);

        return order.getId();
    }

    @Override
    public long addDishToOrder(long orderId, long dishId, List<Long> addedIngredientIds) {
        Optional<Order> maybeOrder = orderRepository.findById(orderId);
        if (maybeOrder.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        Order order = maybeOrder.get();
        Optional<Dish> maybeDish = dishRepository.getDishById(order.getRestaurant().getId(), dishId);
        if (maybeDish.isEmpty()) {
            throw new IllegalArgumentException("Dish not found");
        }
        Dish dish = maybeDish.get();
        if (!order.getState().equals(OrderState.CREATED)) {
            throw new IllegalArgumentException("Invalid order state");
        }
        if (!dish.isAvailable()) {
            throw new IllegalArgumentException("Dish is not available");
        }
        OrderableDish orderableDish = dish;
        List<Ingredient> optionalDishIngredients = dish.getOptionalIngredients();
        for (Long addedIngredientId : addedIngredientIds) {
            Optional<Ingredient> maybeIngredient = ingredientRepository.getIngredientById(addedIngredientId);
            if (maybeIngredient.isEmpty()) {
                throw new IllegalArgumentException("The ingredient " + addedIngredientId + " does not exist");
            }
            Ingredient ingredient = maybeIngredient.get();
            if (!optionalDishIngredients.contains(ingredient)) {
                throw new IllegalArgumentException("The ingredient " + ingredient + " is not usable in the dish");
            }
            Stock stock = ingredient.getStock();
            if (stock.isEmpty()) {
                throw new IllegalStateException("The ingredient " + ingredient + " has no stock");
            }
            orderableDish = new OrderableDishIngredientDecorator(dish, ingredient);
        }
        OrderedDish orderedDish = new OrderedDish(orderableDish.getDish(), orderableDish.getIngredients());
        OrderedDish savedOrderedDish = orderedDishRepository.save(orderedDish);
        List<OrderedDish> orderedDishes = order.getOrderedDishes();
        orderedDishes.add(orderedDish);
        order.setOrderedDishes(orderedDishes);
        orderRepository.save(order);
        return savedOrderedDish.getId();
    }

    @Override
    public void queueOrder(long orderId) {
        Optional<Order> maybeOrder = orderRepository.findById(orderId);
        if (maybeOrder.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        Order order = maybeOrder.get();
        if (!order.getState().equals(OrderState.CREATED)) {
            throw new IllegalArgumentException("Invalid order state");
        }
        order.setState(OrderState.QUEUED);
        orderRepository.save(order);
        notifier.notify(newOrderNotificationFactory.createNotification(order));
    }

    private void deliverOrder(Order order) {
        order.setState(OrderState.DELIVERED);
        orderRepository.save(order);
    }

    @Override
    public void deliverOrder(long orderId) {
        Optional<Order> maybeOrder = orderRepository.findById(orderId);
        if (maybeOrder.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        Order order = maybeOrder.get();
        if (!order.getState().equals(OrderState.COOKED)) {
            throw new IllegalArgumentException("Invalid order state");
        }
        deliverOrder(order);
    }

    @Override
    public Bill getBill(long orderId) {
        Optional<Order> maybeOrder = orderRepository.findById(orderId);
        if (maybeOrder.isEmpty()) {
            throw new IllegalArgumentException("Order not found");
        }
        Order order = maybeOrder.get();
        if (!order.getState().equals(OrderState.DELIVERED)) {
            throw new IllegalArgumentException("Invalid order state");
        }
        Table table = order.getTable();
        SimpleBillVisitor billVisitor = new SimpleBillVisitor();
        order.accept(billVisitor);
        table.accept(billVisitor);
        return billVisitor.getBill();
    }

    @Override
    public Optional<Order> getOrderById(long id) {
        return orderRepository.findById(id);
    }

}
