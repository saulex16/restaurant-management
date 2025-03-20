package at.technikum_wien.restaurant_management.service.interfaces;
import at.technikum_wien.restaurant_management.model.Kitchen;
import at.technikum_wien.restaurant_management.model.orders.Order;

public interface KitchenService {

    public void getNextOrder(long kitchenId);
    public void cookOrder(long orderId);
    public void setOrderReady (long orderId);
}
