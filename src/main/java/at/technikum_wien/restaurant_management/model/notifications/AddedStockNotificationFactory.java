package at.technikum_wien.restaurant_management.model.notifications;

import at.technikum_wien.restaurant_management.model.stock.Stock;

public class AddedStockNotificationFactory extends NotificationFactory<Stock> {

    @Override
    public Notification<Stock> createNotification(Stock payload) {
        return new AddedStockNotification(payload);
    }
}
