package at.technikum_wien.restaurant_management.model.notifications;

import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;

public class OrderReadyNotificationFactory extends NotificationFactory<OrderedDish> {

    @Override
    public Notification<OrderedDish> createNotification(OrderedDish payload) {
        return new OrderReadyNotification(payload);
    }
}
