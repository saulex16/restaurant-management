package at.technikum_wien.restaurant_management.service.interfaces;

import at.technikum_wien.restaurant_management.model.Dish;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Menu;

import java.util.List;
import java.util.Optional;

public interface DishService {
    Dish createDish(long restaurantId, List<Ingredient> baseIngredients, List<Ingredient> optionalIngredients, int durationInMinutes, double markup);
    Optional<Dish> getDishById(long restaurantId, Long id);
    Optional<Menu> getMenu(long restaurantId);
    Dish updateDish(long restaurantId, Dish dish);
    void deleteDish(long restaurantId, Long id);
}
