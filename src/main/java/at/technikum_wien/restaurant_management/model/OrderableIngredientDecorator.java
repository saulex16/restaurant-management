package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.interfaces.OrderableDish;
import lombok.Getter;

@Getter
public class OrderableIngredientDecorator implements OrderableDish {

    private Ingredient ingredient;
    private OrderableDish orderableDish;

    public OrderableIngredientDecorator(OrderableDish orderableDish, Ingredient ingredient) {
        this.ingredient = ingredient;
        this.orderableDish = orderableDish;
    }
}
