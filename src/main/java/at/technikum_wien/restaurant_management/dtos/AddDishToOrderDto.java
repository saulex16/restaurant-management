package at.technikum_wien.restaurant_management.dtos;

import lombok.Getter;

import java.util.List;

@Getter
public class AddDishToOrderDto {
    private long dishId;
    private List<Long> addedIngredientIds;
}
