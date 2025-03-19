package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.notifications.*;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.service.interfaces.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class ManagerServiceImpl implements ManagerService, Observer<Stock> {

    private static final int RESTOCK_QUANTITY = 10;

    private final TaskScheduler taskScheduler;

    private final AddedStockNotificationFactory addedStockNotificationFactory;

    private final Notifier notifier;

    public ManagerServiceImpl(TaskScheduler taskScheduler, AddedStockNotificationFactory addedStockNotificationFactory, Notifier notifier) {
        this.taskScheduler = taskScheduler;
        this.addedStockNotificationFactory = addedStockNotificationFactory;
        this.notifier = notifier;
    }


    @Override
    public void buyIngredient(Ingredient ingredient) {
        Stock stock = new Stock(ingredient, RESTOCK_QUANTITY);
        log.info("Buying {} with stock " + RESTOCK_QUANTITY, ingredient.getName());
        Notification<Stock> notification = addedStockNotificationFactory.createNotification(stock);
        notifier.notify(notification);
    }

    @Override
    public void notify(Notification<Stock> notification) {
        Ingredient ingredient = notification.getPayload().getIngredient();
        taskScheduler.schedule(() -> buyIngredient(ingredient), Instant.now().plusSeconds(30));
    }

    @Override
    public List<NotificationType> getNotificationTypes() {
        return List.of(NotificationType.OUT_OF_STOCK);
    }
}
