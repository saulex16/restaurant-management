package at.technikum_wien.restaurant_management.model.interfaces;

import at.technikum_wien.restaurant_management.model.Ingredient;

import java.util.List;

public interface OrderableDish {
    double getTotalPrice();
    List<Ingredient> getIngredients();
}
