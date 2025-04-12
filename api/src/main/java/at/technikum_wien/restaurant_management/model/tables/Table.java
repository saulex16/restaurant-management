package at.technikum_wien.restaurant_management.model.tables;

import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.bills.BillVisitor;
import at.technikum_wien.restaurant_management.model.bills.Billable;
import at.technikum_wien.restaurant_management.model.orders.Order;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@jakarta.persistence.Table(name = "restaurant_table")
public class Table implements Billable {

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

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = true)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Table() {}

    public Table(String name, TableType tableType, double price) {
        this.name = name;
        this.tableType = tableType;
        this.price = price;
    }

    public boolean isOccupied() {
        return order != null;
    }

    @Override
    public void accept(BillVisitor visitor) {
        visitor.visitTable(this);
    }
}
