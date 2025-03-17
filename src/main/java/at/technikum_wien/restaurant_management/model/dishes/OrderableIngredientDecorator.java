package at.technikum_wien.restaurant_management.model.dishes;

import at.technikum_wien.restaurant_management.model.Ingredient;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderableIngredientDecorator implements OrderableDish {

    private final Ingredient ingredient;
    private final OrderableDish orderableDish;

    public OrderableIngredientDecorator(OrderableDish orderableDish, Ingredient ingredient) {
        this.ingredient = ingredient;
        this.orderableDish = orderableDish;
    }

    @Override
    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredients = orderableDish.getIngredients();
        ingredients.add(ingredient);
        return ingredients;
    }

    @Override
    public Dish getDish() {
        return orderableDish.getDish();
    }
}
