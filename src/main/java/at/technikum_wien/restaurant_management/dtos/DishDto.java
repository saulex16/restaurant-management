package at.technikum_wien.restaurant_management.dtos;

import at.technikum_wien.restaurant_management.model.Dish;
import at.technikum_wien.restaurant_management.model.Ingredient;

import java.util.List;

public class DishDto {
    private String name;
    private List<Ingredient> baseIngredients;
    private List<Ingredient> optionalIngredients;
    private int durationInMinutes;
    private double markup; // The added price to ingredients costs

    public DishDto() { // Required for Jackson
    }

    public static  DishDto fromDish(final Dish dish) {
        DishDto dto = new DishDto();

        dto.setName(dish.getName());
        dto.setBaseIngredients(dish.getBaseIngredients());
        dto.setOptionalIngredients(dish.getOptionalIngredients());
        dto.setDurationInMinutes(dish.getDurationInMinutes());
        dto.setMarkup(dish.getMarkup());

        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getBaseIngredients() {
        return baseIngredients;
    }

    public void setBaseIngredients(List<Ingredient> baseIngredients) {
        this.baseIngredients = baseIngredients;
    }

    public List<Ingredient> getOptionalIngredients() {
        return optionalIngredients;
    }

    public void setOptionalIngredients(List<Ingredient> optionalIngredients) {
        this.optionalIngredients = optionalIngredients;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    public double getMarkup() {
        return markup;
    }

    public void setMarkup(double markup) {
        this.markup = markup;
    }
}
