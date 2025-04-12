package at.technikum_wien.restaurant_management.model.dishes;

import at.technikum_wien.restaurant_management.model.Ingredient;

import java.util.List;

public interface OrderableDish {
    List<Ingredient> getIngredients();
    Dish getDish();
}
