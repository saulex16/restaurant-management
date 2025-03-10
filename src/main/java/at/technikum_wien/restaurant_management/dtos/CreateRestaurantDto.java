package at.technikum_wien.restaurant_management.dtos;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateRestaurantDto {
    private String name;
    private String managerName;
    private Double vipTablePrice;
    private Integer kitchenLimit;
    private List<String> waiterNames;
}
