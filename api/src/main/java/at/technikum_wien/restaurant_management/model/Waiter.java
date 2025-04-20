package at.technikum_wien.restaurant_management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "waiter")
public class Waiter {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "waiter_id_generator")
    @SequenceGenerator(name = "waiter_id_generator", sequenceName = "waiter_id_seq", allocationSize = 1)
    @Column(name = "waiter_id", updatable = false, nullable = false)
    private int id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = true)
    private Restaurant restaurant;

    public Waiter(String name) {
        this.name = name;
    }

    public Waiter() {}
}
