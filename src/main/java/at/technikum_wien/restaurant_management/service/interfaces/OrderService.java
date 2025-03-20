package at.technikum_wien.restaurant_management.service.interfaces;

import at.technikum_wien.restaurant_management.model.bills.Bill;
import at.technikum_wien.restaurant_management.model.orders.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    long createOrder(long restaurantId, long tableId, long waiterId);
    long addDishToOrder(long orderId, long dishId, List<Long> addedIngredientIds);
    void queueOrder(long orderId);
    void deliverOrder(long orderId); // Done automatically by the service impl when notified
    Bill getBill(long orderId);
    Optional<Order> getOrderById(long id);
//    Order updateOrderedDish(long orderId, long orderedDishId);
//    void deleteOrderedDishFromOrder(long orderId, long orderedDishId);
}
