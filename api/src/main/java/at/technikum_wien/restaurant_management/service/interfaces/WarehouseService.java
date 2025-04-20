package at.technikum_wien.restaurant_management.service.interfaces;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.model.Warehouse;

import java.util.Optional;

public interface WarehouseService {
    Warehouse createWarehouse(Restaurant restaurant);
    Optional<Warehouse> getWarehouse(Long id);
    Stock createStockForWarehouse(Ingredient ingredient, Long quantity, Long warehouseId);
    Stock decreaseStock(Long id);
    Stock addStock(Long id, Long quantity);
    Optional<Stock> getStock(Long id);
    boolean dishHasStock(Dish dish);
}
