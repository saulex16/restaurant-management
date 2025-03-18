package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.notifications.*;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.model.Warehouse;
import at.technikum_wien.restaurant_management.repository.interfaces.WarehouseRepository;
import at.technikum_wien.restaurant_management.service.interfaces.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final LowStockNotificationFactory lowStockNotificationFactory;
    private final AddedStockNotificationFactory addedStockNotificationFactory;
    private final Notifier notifier;

    @Autowired
    public WarehouseServiceImpl(
            WarehouseRepository warehouseRepository,
            Notifier notifier,
            LowStockNotificationFactory lowStockNotificationFactory,
            AddedStockNotificationFactory addedStockNotificationFactory
    ) {
        this.warehouseRepository = warehouseRepository;
        this.notifier = notifier;
        this.lowStockNotificationFactory = lowStockNotificationFactory;
        this.addedStockNotificationFactory = addedStockNotificationFactory;
    }


    @Override
    public Warehouse createWarehouse(Restaurant restaurant) {
        return null;
    }

    @Override
    public Warehouse getWarehouse(Long id) {
        return null;
    }

    @Override
    public Stock createStockForWarehouse(Ingredient ingredient, Long quantity, Warehouse warehouse) {
        return null;
    }

    @Override
    public Stock decreaseStock(Long id) {
        Optional<Stock> maybeStock = warehouseRepository.getStock(id);
        if(maybeStock.isEmpty()){
            throw new IllegalArgumentException("Stock with ID: " + id + "doesn't exists");
        }

        Stock stock = maybeStock.get();

        long quantity = stock.getQuantity();
        if(quantity > 0){
            stock.setQuantity(quantity-1);
        } else {
            throw new IllegalArgumentException("There's already no stock for Ingredient:" + stock.getIngredient().getId());
        }

        if (stock.getQuantity() == 0) {
            notifier.notify(lowStockNotificationFactory.createNotification(stock));
        }
        return stock;
    }

    @Override
    public Stock addStock(Long id, Long quantity) {
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be greater or equal than 1");
        }
        Optional<Stock> maybeStock = warehouseRepository.getStock(id);
        if (maybeStock.isEmpty()) {
            throw new IllegalArgumentException("Stock with ID: " + id + "doesn't exists");
        }
        Stock stock = maybeStock.get();

        long currentQuantity = stock.getQuantity();
        currentQuantity += quantity;
        stock.setQuantity(currentQuantity);
        warehouseRepository.updateStock(stock);

        notifier.notify(addedStockNotificationFactory.createNotification(stock));

        return stock;
    }

    @Override
    public Stock getStock(Long id) {
        Optional<Stock> maybeStock = warehouseRepository.getStock(id);
        if (maybeStock.isEmpty()) {
            throw new IllegalArgumentException("Stock with ID: " + id + "doesn't exists");
        }
        return maybeStock.get();
    }
}
