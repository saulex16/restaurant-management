package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.notifications.Notification;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import lombok.Getter;

@Getter
public class LowStockAlert extends Notification {
    private Stock lowStock;
}
