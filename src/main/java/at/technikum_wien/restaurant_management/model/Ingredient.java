package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.dishes.Dish;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ingredient")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_id_generator")
    @SequenceGenerator(name = "ingredient_id_generator", sequenceName = "ingredient_id_seq", allocationSize = 1)
    @Column(name = "ingredient_id", updatable = false, nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "price", nullable = false)
    private double price;

    @ManyToMany
    @JoinTable(
            name = "base_ingredients",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Dish> baseDishes;

    @ManyToMany()
    @JoinTable(
            name = "optional_ingredients",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Dish> optionalDishes;

    public Ingredient (String name, double price){
        this.name = name;
        this.price = price;
    }

    public Ingredient() {}
}
