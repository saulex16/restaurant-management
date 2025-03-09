package at.technikum_wien.restaurant_management.model;

import lombok.Getter;

@Getter
public class LowStockAlert extends Alert {
    private Stock lowStock;
}
