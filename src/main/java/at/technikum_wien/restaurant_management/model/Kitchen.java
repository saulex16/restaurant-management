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

    @Column(name = "limit", nullable = false)
    private int limit; // The amount of dishes that can be cooked simultaneously

    @OneToOne(mappedBy = "kitchen")
    private Restaurant restaurant;

    public Kitchen(int limit) {
        this.limit = limit;
    }

    public Kitchen() {}
}
