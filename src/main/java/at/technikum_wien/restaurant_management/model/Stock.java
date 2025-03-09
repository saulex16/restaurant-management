package at.technikum_wien.restaurant_management.model;

import lombok.Getter;

@Getter
public class Stock {
    private Ingredient ingredient;
    private long quantity;
}
