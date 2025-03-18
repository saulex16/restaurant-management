package at.technikum_wien.restaurant_management.model.notifications;

public abstract class Notification<T> {

    private final NotificationType notificationType;

    public Notification(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public abstract T getPayload();

    public NotificationType getNotificationType() {
        return notificationType;
    }
}
