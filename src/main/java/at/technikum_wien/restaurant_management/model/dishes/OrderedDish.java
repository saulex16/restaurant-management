package at.technikum_wien.restaurant_management.model.dishes;

import at.technikum_wien.restaurant_management.model.Ingredient;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "ordered_dish")
public class OrderedDish {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ordered_dish_id_generator")
    @SequenceGenerator(name = "ordered_dish_id_generator", sequenceName = "ordered_dish_id_seq", allocationSize = 1)
    @Column(name = "dish_id", updatable = false, nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @ManyToMany
    @JoinTable(
            name = "ordered_dish_ingredients",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> ingredients;

    public OrderedDish() {}

    public OrderedDish(Dish dish, List<Ingredient> ingredients) {
        this.dish = dish;
        this.ingredients = ingredients;
    }

    public double getTotalPrice() {
        return ingredients.stream().map(Ingredient::getPrice).reduce(dish.getMarkup(), Double::sum);
    }
}
