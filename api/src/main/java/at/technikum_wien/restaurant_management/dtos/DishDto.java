package at.technikum_wien.restaurant_management.dtos;

import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.Ingredient;

import java.util.List;
import java.util.stream.Collectors;

public class DishDto {
    private String name;
    private List<Long> baseIngredientsIds;
    private List<Long> optionalIngredientsIds;
    private int durationInMinutes;
    private double markup; // The added price to ingredients costs

    public DishDto() { // Required for Jackson
    }

    public static  DishDto fromDish(final Dish dish) {
        DishDto dto = new DishDto();

        dto.setName(dish.getName());
        dto.setDurationInMinutes(dish.getDurationInMinutes());
        dto.setMarkup(dish.getMarkup());

        dto.setBaseIngredientsIds(dish
                .getBaseIngredients()
                .stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList()));

        dto.setOptionalIngredientsIds(dish
                .getOptionalIngredients()
                .stream()
                .map(Ingredient::getId)
                .collect(Collectors.toList()));

        return dto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Long> getBaseIngredientsIds() {
        return baseIngredientsIds;
    }

    public void setBaseIngredientsIds(List<Long> baseIngredientsIds) {
        this.baseIngredientsIds = baseIngredientsIds;
    }

    public List<Long> getOptionalIngredientsIds() {
        return optionalIngredientsIds;
    }

    public void setOptionalIngredientsIds(List<Long> optionalIngredientsIds) {
        this.optionalIngredientsIds = optionalIngredientsIds;
    }
}
