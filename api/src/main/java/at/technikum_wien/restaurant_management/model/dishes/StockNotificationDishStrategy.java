package at.technikum_wien.restaurant_management.model.dishes;

import at.technikum_wien.restaurant_management.model.notifications.Notification;
import at.technikum_wien.restaurant_management.model.stock.Stock;

public interface StockNotificationDishStrategy {
    void processNotification(Notification<Stock> stockNotification);
}
