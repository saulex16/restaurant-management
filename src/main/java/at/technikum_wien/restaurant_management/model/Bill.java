package at.technikum_wien.restaurant_management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;


public class Bill {

    private long id;

    private LocalDateTime dateTime;

    private Map<String, Integer> orderItems;

}
