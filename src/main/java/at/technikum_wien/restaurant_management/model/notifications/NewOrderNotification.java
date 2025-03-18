package at.technikum_wien.restaurant_management.model.notifications;

import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;

public class NewOrderNotification extends Notification<OrderedDish> {

    private final OrderedDish orderedDish;

    public NewOrderNotification(OrderedDish orderedDish) {
        super(NotificationType.NEW_ORDER);
        this.orderedDish = orderedDish;
    }

    @Override
    OrderedDish getPayload() {
        return this.orderedDish;
    }
}
