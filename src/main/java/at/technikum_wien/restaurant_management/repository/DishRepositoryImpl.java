package at.technikum_wien.restaurant_management.repository;

import at.technikum_wien.restaurant_management.model.Dish;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Menu;
import at.technikum_wien.restaurant_management.repository.interfaces.DishRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public class DishRepositoryImpl implements DishRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Dish createDish(long restaurantId, List<Ingredient> baseIngredients, List<Ingredient> optionalIngredients, int durationInMinutes, double markup) {
        return null;
    }

    @Override
    public Optional<Menu> getMenu(long restaurantId) {
        return Optional.empty();
    }

    @Override
    public Optional<Dish> getDishById(long restaurantId, Long id) {
        return Optional.empty();
    }

    @Override
    public void deleteDish(long restaurantId, Long id) {

    }

    @Override
    public Dish updateDish(long restaurantId, Dish dish) {
        return null;
    }
}
