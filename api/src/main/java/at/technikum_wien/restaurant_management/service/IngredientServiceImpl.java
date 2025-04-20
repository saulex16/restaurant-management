package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.repository.interfaces.IngredientRepository;
import at.technikum_wien.restaurant_management.service.interfaces.IngredientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IngredientServiceImpl implements IngredientService {


    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientServiceImpl(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Ingredient createIngredient(String name, double price) {
        return ingredientRepository.createIngredient(name, price);
    }

    @Override
    public Optional<Ingredient> getIngredientById(Long id) {
        return ingredientRepository.getIngredientById(id);
    }

    @Override
    public Ingredient updateIngredient(Long id, Optional<String> name, Optional<Double> price) {

        Ingredient ingredient = null;

        if(name.isPresent()) {
            ingredient = ingredientRepository.updateIngredientName(id, name.get());
        }

        if(price.isPresent()) {
            ingredient = ingredientRepository.updateIngredientPrice(id, price.get());
        }

        if(ingredient != null) {
            return ingredient;
        } else {
            throw new IllegalArgumentException("No updates to ingredient");
        }
    }

    @Override
    public void deleteIngredient(Long id) {
        ingredientRepository.deleteIngredient(id);
    }
}
