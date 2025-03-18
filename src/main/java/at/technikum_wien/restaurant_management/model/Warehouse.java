package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.stock.Stock;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "warehouse_id_generator")
    @SequenceGenerator(name = "warehouse_id_generator", sequenceName = "warehouse_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @OneToMany(mappedBy = "warehouse")
    private List<Stock> stock;

    @OneToOne(mappedBy = "warehouse")
    private Restaurant restaurant;

    public Warehouse() {}
}
