package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.Menu;
import at.technikum_wien.restaurant_management.model.dishes.StockNotificationDishStrategy;
import at.technikum_wien.restaurant_management.model.dishes.StrategyManager;
import at.technikum_wien.restaurant_management.model.notifications.Notification;
import at.technikum_wien.restaurant_management.model.notifications.NotificationType;
import at.technikum_wien.restaurant_management.model.notifications.Notifier;
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
    private final StrategyManager strategyManager;
    private final Notifier notifier;

    @Autowired
    public DishServiceImpl(DishRepository dishRepository, IngredientRepository ingredientRepository, StrategyManager strategyManager, Notifier notifier) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
        this.strategyManager = strategyManager;
        this.notifier = notifier;

        List<NotificationType> notificationTypes = this.getNotificationTypes();
        for (NotificationType type: notificationTypes){
            notifier.subscribe(type, this);
        }
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
        StockNotificationDishStrategy strategy = strategyManager.getStrategy(notification.getNotificationType());
        strategy.processNotification(notification);
    }

    @Override
    public List<NotificationType> getNotificationTypes() {
        return List.of(NotificationType.OUT_OF_STOCK, NotificationType.ADDED_STOCK);
    }

}
