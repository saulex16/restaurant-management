package at.technikum_wien.restaurant_management.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class Bill {
    private long id;
    private LocalDateTime dateTime;
    private Map<String, Integer> orderItems;
}
