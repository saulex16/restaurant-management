package at.technikum_wien.restaurant_management.model.dishes;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Menu;

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

    @ManyToMany
    @JoinTable(
            name = "base_ingredients",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> baseIngredients;

    @ManyToMany
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
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @OneToMany(mappedBy = "dish")
    private List<OrderedDish> orderedDishes;

    public Dish(String name, int duration, double markup, List<Ingredient> optionalIngredients, List<Ingredient> baseIngredients){
        this.name = name;
        this.durationInMinutes = duration;
        this.markup = markup;
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
