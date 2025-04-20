package at.technikum_wien.restaurant_management.dtos;

import at.technikum_wien.restaurant_management.model.Ingredient;

public class IngredientDto {

    private Long id;
    private String name;
    private double price;

    public static IngredientDto fromIngredient(Ingredient ingredient) {
        IngredientDto dto = new IngredientDto();
        dto.name = ingredient.getName();
        dto.price = ingredient.getPrice();
        dto.id = ingredient.getId();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
