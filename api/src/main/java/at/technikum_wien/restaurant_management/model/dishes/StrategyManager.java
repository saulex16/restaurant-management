package at.technikum_wien.restaurant_management.model.dishes;


import at.technikum_wien.restaurant_management.model.notifications.NotificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StrategyManager {
    private final Map<NotificationType, StockNotificationDishStrategy> strategies = new HashMap<>();

    @Autowired
    public StrategyManager(List<StockNotificationDishStrategy> strategyList) {
        for (StockNotificationDishStrategy strategy : strategyList) {
            if (strategy instanceof OutOfStockNotificationDishStrategy) {
                strategies.put(NotificationType.OUT_OF_STOCK, strategy);
            } else if (strategy instanceof AddedStockNotificationDishStrategy) {
                strategies.put(NotificationType.ADDED_STOCK, strategy);
            } else {
                throw new IllegalArgumentException("Strategy not supported");
            }
        }
    }

    public StockNotificationDishStrategy getStrategy(NotificationType notificationType) {
        if (!strategies.containsKey(notificationType)) {
            throw new IllegalArgumentException("No strategy found for: " + notificationType);
        }
        return strategies.get(notificationType);
    }
}


