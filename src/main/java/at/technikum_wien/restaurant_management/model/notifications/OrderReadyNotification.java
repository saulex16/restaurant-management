package at.technikum_wien.restaurant_management.model.notifications;

import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;

public class OrderReadyNotification extends Notification<OrderedDish> {

    private final OrderedDish orderedDish;

    public OrderReadyNotification(OrderedDish orderedDish) {
        super(NotificationType.ORDER_READY);
        this.orderedDish = orderedDish;
    }

    @Override
    public OrderedDish getPayload() {
        return this.orderedDish;
    }
}
