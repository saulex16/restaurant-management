package at.technikum_wien.restaurant_management.model.orders;

import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.Waiter;
import at.technikum_wien.restaurant_management.model.bills.BillVisitor;
import at.technikum_wien.restaurant_management.model.bills.Billable;
import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;
import at.technikum_wien.restaurant_management.model.tables.Table;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@jakarta.persistence.Table(name = "restaurant_order")
public class Order implements Billable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
    @SequenceGenerator(name = "order_id_generator", sequenceName = "order_id_seq", allocationSize = 1)
    @Column(name = "order_id", updatable = false, nullable = false)
    private long id;

    @ManyToMany
    @JoinTable(
            name = "order_dishes",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id"))
    private List<OrderedDish> orderedDishes;

    @OneToOne(mappedBy = "order")
    private Table table;

    @ManyToOne
    @JoinColumn(name = "waiter_id", nullable = false)
    private Waiter waiter;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private OrderState state;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Order(Restaurant restaurant, Table table, Waiter waiter, OrderState state) {
        this.orderedDishes = new ArrayList<>();
        this.restaurant = restaurant;
        this.table = table;
        this.waiter = waiter;
        this.state = state;
    }

    public Order() { }

    @Override
    public void accept(BillVisitor visitor) {
        visitor.visitOrder(this);
    }
}
