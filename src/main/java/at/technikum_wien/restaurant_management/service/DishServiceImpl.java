package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.Menu;
import at.technikum_wien.restaurant_management.model.notifications.LowStockNotification;
import at.technikum_wien.restaurant_management.model.notifications.Notification;
import at.technikum_wien.restaurant_management.model.notifications.NotificationType;
import at.technikum_wien.restaurant_management.model.notifications.Observer;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.repository.interfaces.DishRepository;
import at.technikum_wien.restaurant_management.repository.interfaces.IngredientRepository;
import at.technikum_wien.restaurant_management.service.interfaces.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DishServiceImpl implements DishService, Observer<Stock> {

    private final DishRepository dishRepository;

    private final IngredientRepository ingredientRepository;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Transactional
    public Dish createDish(long restaurantId, String name, List<Long> baseIngredientsIds, List<Long> optionalIngredientsIds, int durationInMinutes, double markup) {

        List<Ingredient> baseIngredients = ingredientRepository.getAllIngredientsById(baseIngredientsIds);
        List<Ingredient> optionalIngredients = ingredientRepository.getAllIngredientsById(optionalIngredientsIds);

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

    @Override
    public void notify(Notification<Stock> notification) {
        Stock stock = notification.getPayload();
        Strategy strategy = strategyManager.getStrategy(notification.getNotificationType());
        strategy.process(notification.getPayload());
        int quantity =

    }


    @Override
    public List<NotificationType> getNotificationTypes() {
        return List.of(NotificationType.LOW_STOCK, NotificationType.ADDED_STOCK);
    }

    public void onStockDepleted(Ingredient ingredient) {
        for (Dish dish: ingredient.getBaseDishes()){
            dish.setAvailable(false);
            dishRepository.updateDish(dish.getRestaurant().getId(), dish);
        }

        for (Dish dish: ingredient.getOptionalDishes()){
            dish.setAvailable(false);
            dishRepository.updateDish(dish.getRestaurant().getId(), dish);
        }
    }


}
