package at.technikum_wien.restaurant_management.repository.interfaces;

import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends JpaRepository<Dish, Long> {
    void createDish(long restaurantId, List<Ingredient> baseIngredients, List<Ingredient> optionalIngredients, int durationInMinutes, double markup);
    Optional<Dish> getDishById(long restaurantId, Long id);
    Optional<Menu> getMenu(long restaurantId);
    Dish updateDish(long restaurantId, Dish dish);
    void deleteDish(long restaurantId, Long id);
}
