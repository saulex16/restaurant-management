package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Dish;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Menu;
import at.technikum_wien.restaurant_management.repository.interfaces.DishRepository;
import at.technikum_wien.restaurant_management.service.interfaces.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public Dish createDish(long restaurantId, String name, List<Ingredient> baseIngredients, List<Ingredient> optionalIngredients, int durationInMinutes, double markup) {
        return dishRepository.createDish(restaurantId, name, baseIngredients, optionalIngredients, durationInMinutes, markup);
    }

    @Override
    public Optional<Dish> getDishById(long restaurantId, Long id) {
        return dishRepository.getDishById(restaurantId, id);
    }

    @Override
    public Optional<Menu> getMenu(long restaurantId) {
        return dishRepository.getMenu(restaurantId);
    }

    @Override
    public Dish updateDish(long restaurantId, Dish dish) {
        return dishRepository.updateDish(restaurantId, dish);
    }

    @Override
    public void deleteDish(long restaurantId, Long id) {
        dishRepository.deleteDish(restaurantId, id);
    }
}
