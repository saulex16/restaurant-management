package at.technikum_wien.restaurant_management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "manager_id_generator")
    @SequenceGenerator(name = "manager_id_generator", sequenceName = "manager_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @OneToOne(mappedBy = "manager")
    private Restaurant restaurant;

    public Manager(String name) {
        this.name = name;
    }

    public Manager() {}
}
