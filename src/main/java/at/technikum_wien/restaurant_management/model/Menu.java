package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.dishes.Dish;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menu_id_generator")
    @SequenceGenerator(name = "menu_id_generator", sequenceName = "menu_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @OneToMany(mappedBy = "menu")
    private List<Dish> dishes;

    @OneToOne(mappedBy = "menu")
    private Restaurant restaurant;
}
