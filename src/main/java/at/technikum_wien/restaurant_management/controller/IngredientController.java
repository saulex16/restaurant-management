package at.technikum_wien.restaurant_management.controller;

import at.technikum_wien.restaurant_management.dtos.IngredientDto;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.service.interfaces.IngredientService;
import at.technikum_wien.restaurant_management.utils.Endpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(Endpoints.INGREDIENTS)
public class IngredientController {

    private final IngredientService ingredientService;

    @Autowired
    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<IngredientDto> createIngredient(@RequestBody IngredientDto ingredientDto) {
        Ingredient ingredient = ingredientService.createIngredient(ingredientDto.getName(), ingredientDto.getPrice());
        return ResponseEntity.ok(IngredientDto.fromIngredient(ingredient));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngredientDto> getIngredient(@PathVariable Long id) {
        Optional<Ingredient> maybeIngredient = ingredientService.getIngredientById(id);
        return maybeIngredient.map(ingredient -> ResponseEntity.ok(IngredientDto.fromIngredient(ingredient)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<IngredientDto> updateIngredient(@PathVariable Long id, @RequestBody IngredientDto ingredientDto) {
        Ingredient ingredient = ingredientService.updateIngredient(id, Optional.ofNullable(ingredientDto.getName()),
                    Optional.of(ingredientDto.getPrice()));

        return ResponseEntity.ok(IngredientDto.fromIngredient(ingredient));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable Long id) {
        //TODO: Exception mapper should manage the case the ingredient doesnt exists
        ingredientService.deleteIngredient(id);
        return ResponseEntity.noContent().build();
    }

}
