package at.technikum_wien.restaurant_management.model.notifications;

import java.util.List;

public interface Observer<T> {
    void notify(Notification<T> notification);
    List<NotificationType> getNotificationTypes();
}
