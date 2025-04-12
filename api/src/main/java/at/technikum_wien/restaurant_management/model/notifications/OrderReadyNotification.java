package at.technikum_wien.restaurant_management.model.notifications;

import at.technikum_wien.restaurant_management.model.orders.Order;

public class OrderReadyNotification extends Notification<Order> {

    private final Order order;

    public OrderReadyNotification(Order order) {
        super(NotificationType.ORDER_READY);
        this.order = order;
    }

    @Override
    public Order getPayload() {
        return this.order;
    }
}
