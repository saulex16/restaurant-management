package at.technikum_wien.restaurant_management.model.notification;

public abstract class BasicNotifier implements Notifier {

    @Override
    abstract public void send();

    public void notify(String message) {
        // TODO: Implement notification logic
        System.out.println(message);
    }
}
