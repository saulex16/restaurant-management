package at.technikum_wien.restaurant_management.model.notifications;

import at.technikum_wien.restaurant_management.model.orders.Order;

public class NewOrderNotification extends Notification<Order> {

    private final Order order;

    public NewOrderNotification(Order orderedDish) {
        super(NotificationType.NEW_ORDER);
        this.order = orderedDish;
    }

    @Override
    public Order getPayload() {
        return this.order;
    }
}
