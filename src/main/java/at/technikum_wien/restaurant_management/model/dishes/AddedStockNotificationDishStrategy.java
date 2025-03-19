package at.technikum_wien.restaurant_management.model.dishes;


import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.notifications.Notification;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.repository.interfaces.DishRepository;
import at.technikum_wien.restaurant_management.service.interfaces.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddedStockNotificationDishStrategy implements StockNotificationDishStrategy{
    private final DishRepository dishRepository;

    private final WarehouseService warehouseService;

    @Autowired
    public AddedStockNotificationDishStrategy(DishRepository dishRepository, WarehouseService warehouseService) {
        this.dishRepository = dishRepository;
        this.warehouseService = warehouseService;
    }

    @Override
    public void processNotification(Notification<Stock> stockNotification) {
        Ingredient ingredient = stockNotification.getPayload().getIngredient();
        List<Dish> baseDishes = ingredient.getBaseDishes();

        for (Dish dish : baseDishes) {
            if (!dish.isAvailable()) {
                boolean allIngredientsAvailable = warehouseService.dishHasStock(dish);
                if (allIngredientsAvailable){
                    dish.setAvailable(true);
                    dishRepository.updateDish(dish.getRestaurant().getId(), dish);
                }
            }

        }
    }
}
