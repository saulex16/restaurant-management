package at.technikum_wien.restaurant_management.model.notifications;

import at.technikum_wien.restaurant_management.model.stock.Stock;

public class AddedStockNotification extends Notification<Stock> {

    private final Stock stock;

    public AddedStockNotification(Stock stock) {
        super(NotificationType.ADDED_STOCK);
        this.stock = stock;
    }

    @Override
    public Stock getPayload() {
        return this.stock;
    }
}
