package at.technikum_wien.restaurant_management.repository.interfaces;

import at.technikum_wien.restaurant_management.model.Ingredient;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository {
    Ingredient createIngredient(String name, double price);
    Optional<Ingredient> getIngredientById(Long id);
    List<Ingredient> getAllIngredientsById(List<Long> ids);
    Ingredient updateIngredientName(Long id, String name);
    Ingredient updateIngredientPrice(Long id, double price);
    void deleteIngredient(Long id);
}
