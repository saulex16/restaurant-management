package at.technikum_wien.restaurant_management.repository;

import at.technikum_wien.restaurant_management.model.Dish;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Menu;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.repository.interfaces.DishRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
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
    public Dish createDish(long restaurantId, String name, List<Ingredient> baseIngredients, List<Ingredient> optionalIngredients, int durationInMinutes, double markup) {
        //TODO: Use restaurantRepository
        Restaurant restaurant = entityManager.find(Restaurant.class, restaurantId);
        Dish dish = new Dish(name, baseIngredients, optionalIngredients, durationInMinutes, markup, restaurant);

        entityManager.persist(dish);
        return dish;
    }

    @Override
    public Optional<Menu> getMenu(long restaurantId) {
        Restaurant restaurant = entityManager.find(Restaurant.class, restaurantId);

        TypedQuery<Dish> query = entityManager.createQuery(
                "SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId", Dish.class);
        query.setParameter("restaurantId", restaurantId);

        List<Dish> dishes = query.getResultList();
        if (dishes.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(new Menu(dishes, restaurant));
        }
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
