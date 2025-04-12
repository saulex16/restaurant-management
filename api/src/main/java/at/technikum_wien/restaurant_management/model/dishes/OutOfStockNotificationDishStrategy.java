package at.technikum_wien.restaurant_management.model.dishes;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.notifications.Notification;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.repository.interfaces.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutOfStockNotificationDishStrategy implements StockNotificationDishStrategy {

    private final DishRepository dishRepository;

    @Autowired
    public OutOfStockNotificationDishStrategy(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public void processNotification(Notification<Stock> stockNotification) {
        Ingredient ingredient = stockNotification.getPayload().getIngredient();
        List<Dish> baseDishes = ingredient.getBaseDishes();

        for (Dish dish : baseDishes) {
            if (!dish.isAvailable()) {
                continue;
            }
            dish.setAvailable(false);
            dishRepository.updateDish(dish.getRestaurant().getId(), dish);
        }
    }
}
