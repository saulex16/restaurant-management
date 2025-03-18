package at.technikum_wien.restaurant_management.model.notifications;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Notifier {

    Map<NotificationType, List<Observer<?>>> subscriptions;

    public Notifier() {
        this.subscriptions = new HashMap<>();
    }

    public <T> void subscribe(NotificationType type, Observer<T> subscriber){

        List<Observer<T>> subscribers = (List<Observer<T>>)(List<?>) subscriptions.getOrDefault(type, new ArrayList<>());
        subscribers.add(subscriber);
        subscriptions.putIfAbsent(type, (List<Observer<?>>)(List<?>)subscribers);
    }

    public <T> void notify(Notification<T> notification){
        NotificationType type = notification.getNotificationType();
        List<Observer<T>> subscribers = (List<Observer<T>>)(List<?>) subscriptions.get(type);
        subscribers.forEach(s -> s.notify(notification));
    }
}