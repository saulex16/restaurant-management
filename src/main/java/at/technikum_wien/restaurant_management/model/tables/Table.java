package at.technikum_wien.restaurant_management.model.tables;

import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.orders.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@jakarta.persistence.Table(name = "restaurant_table")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "table_id_generator")
    @SequenceGenerator(name = "table_id_generator", sequenceName = "table_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "table_type", nullable = false)
    private TableType tableType;

    @Column(name = "price", nullable = false)
    private double price;

    @OneToOne(mappedBy = "table")
    private Order order;

    @Column(name = "is_occupied", nullable = false)
    private boolean isOccupied;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Table() {}

    public Table(String name, TableType tableType, double price, boolean isOccupied) {
        this.name = name;
        this.tableType = tableType;
        this.price = price;
        this.isOccupied = isOccupied;
    }
}
