package at.technikum_wien.restaurant_management.service.interfaces;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.model.Warehouse;

public interface WarehouseService {
    Warehouse createWarehouse(Restaurant restaurant);
    Warehouse getWarehouse(Long id);
    Stock createStockForWarehouse(Ingredient ingredient, Long quantity, Warehouse warehouse);
    Stock decreaseStock(Long id);
    Stock addStock(Long id, Long quantity);
    Stock getStock(Long id);
}
