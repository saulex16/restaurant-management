package at.technikum_wien.restaurant_management.service.interfaces;

import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.orders.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    void createOrder(long restaurantId, List<Ingredient> baseIngredients, List<Ingredient> optionalIngredients, int durationInMinutes, double markup);
    Optional<Order> getOrderById(long restaurantId, Long id);
    //Optional<Menu> getMenu(long restaurantId);
    Order updateDish(long restaurantId, Dish dish);
    void deleteOrderDish(long restaurantId, Long dish_id);
}
