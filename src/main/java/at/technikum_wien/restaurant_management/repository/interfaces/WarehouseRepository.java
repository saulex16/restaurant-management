package at.technikum_wien.restaurant_management.repository.interfaces;

import at.technikum_wien.restaurant_management.model.Stock;
import at.technikum_wien.restaurant_management.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse,Long> {
    void createStock(long restaurantId, Stock stock);
    void updateStock(long restaurantId, Stock stock);
}
