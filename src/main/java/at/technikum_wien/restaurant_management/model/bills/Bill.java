package at.technikum_wien.restaurant_management.model.bills;

import java.time.LocalDateTime;
import java.util.Map;


public class Bill {


    private final LocalDateTime dateTime;

    private final Map<String, Double> orderItems;

    public Bill(LocalDateTime dateTime, Map<String, Double> orderItems) {
        this.dateTime = dateTime;
        this.orderItems = orderItems;
    }
}
