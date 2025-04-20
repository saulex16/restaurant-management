package at.technikum_wien.restaurant_management.service.interfaces;

import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.model.tables.TableType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface RestaurantService {
    Restaurant getRestaurant(final long id);
    Restaurant createRestaurant(final String name, final String managerName, final int kitchenLimit, List<String> waiterNames, Optional<Double> vipTablePrice);
    long addWaiter(final long restaurantId, final String waiterName);
    void setVipTablePrice(final long restaurantId, final Double vipTablePrice);

    long addBasicTable(final long restaurantId, final String tableName);
    long addVipTable(final long restaurantId, final String tableName);

    List<Stock> getAllNonEmptyStocksByRestaurantId(final long restaurantId);
}
