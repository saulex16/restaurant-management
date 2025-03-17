package at.technikum_wien.restaurant_management.model.orders;

import at.technikum_wien.restaurant_management.model.Waiter;
import at.technikum_wien.restaurant_management.model.dishes.OrderableDish;
import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;
import at.technikum_wien.restaurant_management.model.tables.Table;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
@jakarta.persistence.Table(name = "order")
public class Order {

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
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private Table table;

    @ManyToOne
    @JoinColumn(name = "waiter_id", nullable = false)
    private Waiter waiter;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private OrderState state;


    public Order(Table table, Waiter waiter, List<OrderedDish> orderedDishes, OrderState state) {
        this.orderedDishes = orderedDishes;
        this.table = table;
        this.waiter = waiter;
        this.state = state;
    }

    public Order() {

    }
}
