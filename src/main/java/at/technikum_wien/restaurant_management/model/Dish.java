package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.interfaces.OrderableDish;
import lombok.Getter;

import java.util.List;

@Getter
public class Dish implements OrderableDish {
    private long id;
    private String name;
    private List<Ingredient> baseIngredients;
    private List<Ingredient> optionalIngredients;
    private int durationInMinutes;
    private double markup; // The added price to ingredients costs
}
