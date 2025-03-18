package at.technikum_wien.restaurant_management.model.dishes;
import at.technikum_wien.restaurant_management.model.Ingredient;

import at.technikum_wien.restaurant_management.model.Restaurant;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Table(name = "dish")
@Setter
public class Dish implements OrderableDish {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_id_generator")
    @SequenceGenerator(name = "dish_id_generator", sequenceName = "dish_id_seq", allocationSize = 1)
    @Column(name = "dish_id", updatable = false, nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "base_ingredients",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> baseIngredients;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "optional_ingredients",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> optionalIngredients;

    @Column(name="duration", nullable = false)
    private int durationInMinutes;

    @Column(name = "markup", nullable = false)
    private double markup; // The added price to ingredients costs

    @ManyToOne
    @JoinColumn(name = "restaurant", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "dish")
    private List<OrderedDish> orderedDishes;

    public Dish() { }

    public Dish(String name, int duration, double markup, List<Ingredient> optionalIngredients, List<Ingredient> baseIngredients, Restaurant restaurant) {
        this.name = name;
        this.baseIngredients = baseIngredients;
        this.optionalIngredients = optionalIngredients;
        this.markup = markup;
        this.durationInMinutes = duration;
        this.restaurant = restaurant;
    }

    @Override
    public List<Ingredient> getIngredients() {
        return List.copyOf(baseIngredients);
    }

    @Override
    public Dish getDish() {
        return this;
    }
}
