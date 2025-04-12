package at.technikum_wien.restaurant_management.repository;

import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Menu;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.repository.interfaces.DishRepository;
import at.technikum_wien.restaurant_management.repository.interfaces.RestaurantRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
@Transactional
public class DishRepositoryImpl implements DishRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private final RestaurantRepository restaurantRepository;

    @Autowired
    public DishRepositoryImpl(EntityManager entityManager, RestaurantRepository restaurantRepository) {
        this.entityManager = entityManager;
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Dish createDish(long restaurantId, String name, List<Ingredient> baseIngredients, List<Ingredient> optionalIngredients, int durationInMinutes, double markup) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

        Dish dish = new Dish(name, durationInMinutes, markup, new ArrayList<>(), new ArrayList<>(), restaurant);

        dish.getBaseIngredients().addAll(baseIngredients);
        dish.getOptionalIngredients().addAll(optionalIngredients);

        entityManager.persist(dish);
        entityManager.flush();

        return dish;
    }

    //TODO: Creating menu is probably business logic and not repository responsibility
    @Override
    public Optional<Menu> getMenu(long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();

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

        TypedQuery<Dish> query = entityManager.createQuery(
                "SELECT d FROM Dish d WHERE d.restaurant.id = :restaurantId AND d.id = :id", Dish.class
        );
        query.setParameter("restaurantId", restaurantId);
        query.setParameter("id", id);

        return  query.getResultList().stream().findFirst();
    }

    @Override
    public void deleteDish(long restaurantId, Long id) {
        Optional<Dish> dish = Optional.ofNullable(entityManager.find(Dish.class, id));

        dish.ifPresentOrElse(
                entityManager::remove,
                () -> { throw new EntityNotFoundException("Dish with ID: " + id + " not found"); }
        );
    }

    @Override
    public Dish updateDish(long restaurantId, Dish dish) {
        Optional<Dish> maybeDish = Optional.ofNullable(entityManager.find(Dish.class, dish.getId()));

        if(maybeDish.isPresent()){
            Dish oldDish = maybeDish.get();
            oldDish.setName(dish.getName());
            oldDish.setMarkup(dish.getMarkup());
            oldDish.setBaseIngredients(dish.getBaseIngredients());
            oldDish.setOptionalIngredients(dish.getOptionalIngredients());
            oldDish.setDurationInMinutes(dish.getDurationInMinutes());
            oldDish.setAvailable(dish.isAvailable());

            entityManager.merge(oldDish);

            return oldDish;
        } else {
            throw new EntityNotFoundException("Dish with ID: " + dish.getId() + " not found");
        }
    }
}
