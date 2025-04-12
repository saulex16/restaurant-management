package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.notifications.*;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.model.Warehouse;
import at.technikum_wien.restaurant_management.repository.interfaces.WarehouseRepository;
import at.technikum_wien.restaurant_management.service.interfaces.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarehouseServiceImpl implements WarehouseService, Observer<Stock> {

    private final WarehouseRepository warehouseRepository;
    private final OutOfStockNotificationFactory outOfStockNotificationFactory;
    private final AddedStockNotificationFactory addedStockNotificationFactory;
    private final Notifier notifier;

    @Autowired
    public WarehouseServiceImpl(
            WarehouseRepository warehouseRepository,
            Notifier notifier,
            OutOfStockNotificationFactory outOfStockNotificationFactory,
            AddedStockNotificationFactory addedStockNotificationFactory
    ) {
        this.warehouseRepository = warehouseRepository;
        this.notifier = notifier;
        this.outOfStockNotificationFactory = outOfStockNotificationFactory;
        this.addedStockNotificationFactory = addedStockNotificationFactory;

        List<NotificationType> notificationTypes = this.getNotificationTypes();
        for (NotificationType type: notificationTypes){
            notifier.subscribe(type, this);
        }
    }


    @Override
    public Warehouse createWarehouse(Restaurant restaurant) {
        return warehouseRepository.createWarehouse(restaurant);
    }

    @Override
    public Optional<Warehouse> getWarehouse(Long id) {
        return warehouseRepository.getWarehouse(id);
    }

    @Override
    public Stock createStockForWarehouse(Ingredient ingredient, Long quantity, Long warehouseId) {
        return warehouseRepository.createStockForWarehouse(ingredient, quantity, warehouseId);
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
            notifier.notify(outOfStockNotificationFactory.createNotification(stock));
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

        return stock;
    }

    @Override
    public Optional<Stock> getStock(Long id) {
        return warehouseRepository.getStock(id);
    }

    @Override
    public boolean dishHasStock(Dish dish) {
        List<Stock> dishStock = warehouseRepository.getStockByDish(dish);
        return dish.getBaseIngredients().size() == dishStock.size();
    }

    @Override
    public void notify(Notification<Stock> notification) {
        Stock stock = notification.getPayload();
        this.addStock(stock.getId(), stock.getQuantity());
    }

    @Override
    public List<NotificationType> getNotificationTypes() {
        return List.of(NotificationType.ADDED_STOCK);
    }
}
