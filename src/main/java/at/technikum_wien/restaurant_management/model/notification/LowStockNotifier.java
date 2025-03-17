package at.technikum_wien.restaurant_management.model.notification;

import at.technikum_wien.restaurant_management.model.Stock;

public class LowStockNotifier extends BasicNotifier {

    private final Stock stock;
    
    public LowStockNotifier(Stock stock) {
        this.stock = stock;
    }

    @Override
    public void send() {
        String message = "Low stock for the following ingredient: " +
                stock.getIngredient().getName() +
                " (" +
                stock.getQuantity() +
                " units left)";
        super.notify(message);
    }
}
