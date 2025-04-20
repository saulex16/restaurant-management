package at.technikum_wien.restaurant_management.service.interfaces;

import at.technikum_wien.restaurant_management.model.Ingredient;

import java.util.Optional;

public interface IngredientService {
    Ingredient createIngredient(String name, double price);
    Optional<Ingredient> getIngredientById(Long id);
    Ingredient updateIngredient(Long id, Optional<String> name, Optional<Double> price);
    void deleteIngredient(Long id);
}
