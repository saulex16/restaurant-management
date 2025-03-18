package at.technikum_wien.restaurant_management.model.notifications;

import at.technikum_wien.restaurant_management.model.stock.Stock;

public class LowStockNotification extends Notification<Stock> {

    private final Stock stock;
    
    public LowStockNotification(Stock stock) {
        super(NotificationType.LOW_STOCK);
        this.stock = stock;
    }

    @Override
    public Stock getPayload() {
        return this.stock;
    }
}
