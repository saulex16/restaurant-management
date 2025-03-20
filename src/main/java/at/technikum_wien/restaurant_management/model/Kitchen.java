package at.technikum_wien.restaurant_management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kitchen")
public class Kitchen {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kitchen_id_generator")
    @SequenceGenerator(name = "kitchen_id_generator", sequenceName = "kitchen_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "dishes_limit", nullable = false)
    private int dishesLimit; // The amount of dishes that can be cooked simultaneously

    @Column(name ="current_dishes", nullable = false)
    private int currentDishes;

    @OneToOne(mappedBy = "kitchen")
    private Restaurant restaurant;

    public Kitchen(int dishesLimit) {
        this.dishesLimit = dishesLimit;
    }

    public Kitchen() {}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Kitchen other)) return false;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
