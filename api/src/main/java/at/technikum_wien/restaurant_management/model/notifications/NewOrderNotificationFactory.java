package at.technikum_wien.restaurant_management.model.notifications;

import at.technikum_wien.restaurant_management.model.orders.Order;
import org.springframework.stereotype.Component;

@Component
public class NewOrderNotificationFactory extends NotificationFactory<Order> {

    @Override
    public Notification<Order> createNotification(Order payload) {
        return new NewOrderNotification(payload);
    }
}
