package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Bill;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.Waiter;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.dishes.OrderableDish;
import at.technikum_wien.restaurant_management.model.dishes.OrderableIngredientDecorator;
import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;
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
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;
    private final TableRepository tableRepository;
    private final WaiterRepository waiterRepository;
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public OrderServiceImpl(
            OrderRepository orderRepository,
            RestaurantRepository restaurantRepository,
            TableRepository tableRepository,
            WaiterRepository waiterRepository,
            DishRepository dishRepository,
            IngredientRepository ingredientRepository
    ) {
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
        this.tableRepository = tableRepository;
        this.waiterRepository = waiterRepository;
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
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
        return order.getId();
    }

    @Override
    public long addDishToOrder(long orderId, long dishId, List<Long> optionalIngredientIds) {
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
        for (Long optionalIngredientId : optionalIngredientIds) {
            Optional<Ingredient> maybeIngredient = ingredientRepository.getIngredientById(optionalIngredientId);
            if (maybeIngredient.isEmpty()) {
                throw new IllegalArgumentException("The ingredient " + optionalIngredientId + " does not exist");
            }
            Ingredient ingredient = maybeIngredient.get();
            if (!optionalDishIngredients.contains(ingredient)) {
                throw new IllegalArgumentException("The ingredient " + ingredient + " is not usable in the dish");
            }
            Stock stock = ingredient.getStock();
            if (stock.isEmpty()) {
                throw new IllegalStateException("The ingredient " + ingredient + " has no stock");
            }
            orderableDish = new OrderableIngredientDecorator(dish, ingredient);
        }
        OrderedDish orderedDish = new OrderedDish(orderableDish.getDish(), orderableDish.getIngredients());
        // TODO: Save orderedDish and add to the order
    }

    @Override
    public void queueOrder(long orderId);

    @Override
    public void deliverOrder(long orderId);

    @Override
    public Bill getBill(long orderId);

    @Override
    public Optional<Order> getOrderById(long restaurantId, long id);

}
