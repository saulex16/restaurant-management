package at.technikum_wien.restaurant_management.model.dishes;


import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.notifications.Notification;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.repository.interfaces.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddedStockNotificationDishStrategy implements StockNotificationDishStrategy{
    private final DishRepository dishRepository;

    @Autowired
    public AddedStockNotificationDishStrategy(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    @Override
    public void processNotification(Notification<Stock> stockNotification) {
        Ingredient ingredient = stockNotification.getPayload().getIngredient();
        List<Dish> baseDishes = ingredient.getBaseDishes();

        for (Dish dish : baseDishes) {
            if (!dish.isAvailable()) {
                boolean allIngredientsAvailable = true;
                List<Ingredient> dishIngredients = dish.getBaseIngredients();
                for (Ingredient ing : dishIngredients){
                    if (ing.getStock().getQuantity() < 1){
                        allIngredientsAvailable = false;
                        break;
                    }
                }
                if (allIngredientsAvailable){
                    dish.setAvailable(true);
                    dishRepository.updateDish(dish.getRestaurant().getId(), dish);
                }
            }

        }
    }
}
