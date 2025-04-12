package at.technikum_wien.restaurant_management.repository.interfaces;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.model.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository{
    Warehouse createWarehouse(Restaurant restaurant);
    Optional<Warehouse> getWarehouse(Long id);
    Stock createStockForWarehouse(Ingredient ingredient, Long quantity, Long warehouseId);
    Stock updateStock(Stock stock);
    Optional<Stock> getStock(Long id);
    List<Stock> getStockByDish(Dish dish);
}
