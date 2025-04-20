package at.technikum_wien.restaurant_management.model.notifications;

import at.technikum_wien.restaurant_management.model.stock.Stock;

public class OutOfStockNotification extends Notification<Stock> {

    private final Stock stock;
    
    public OutOfStockNotification(Stock stock) {
        super(NotificationType.OUT_OF_STOCK);
        this.stock = stock;
    }

    @Override
    public Stock getPayload() {
        return this.stock;
    }
}
