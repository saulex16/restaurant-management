package at.technikum_wien.restaurant_management.service.interfaces;

import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.Menu;

import java.util.List;
import java.util.Optional;

public interface DishService {
    Dish createDish(long restaurantId, String name, List<Long> baseIngredientsIds, List<Long> optionalIngredientsIds, int durationInMinutes, double markup);
    Optional<Dish> getDishById(long restaurantId, Long id);
    Optional<Menu> getMenu(long restaurantId);
    Dish updateDish(long restaurantId, Dish dish);
    void deleteDish(long restaurantId, Long id);
}
