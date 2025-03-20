package at.technikum_wien.restaurant_management.dtos;

import lombok.Getter;

@Getter
public class CreateOrderDto {
    private long restaurantId;
    private long waiterId;
    private long tableId;
}
