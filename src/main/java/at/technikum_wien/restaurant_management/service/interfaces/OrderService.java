package at.technikum_wien.restaurant_management.service.interfaces;

import at.technikum_wien.restaurant_management.model.Bill;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.orders.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    long createOrder(long restaurantId, long tableId, long waiterId);
    public long addDishToOrder(long orderId, long dishId, List<Long> optionalIngredientIds);
    void queueOrder(long orderId);
    void deliverOrder(long orderId);
    Bill getBill(long orderId);
    Optional<Order> getOrderById(long restaurantId, long id);
//    Order updateOrderedDish(long orderId, long orderedDishId);
//    void deleteOrderedDishFromOrder(long orderId, long orderedDishId);
}
