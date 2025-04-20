package at.technikum_wien.restaurant_management.model.notifications;

public abstract class NotificationFactory<T> {

    public abstract Notification<T> createNotification(T payload);

}
