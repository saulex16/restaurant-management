package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.interfaces.OrderableDish;
import at.technikum_wien.restaurant_management.model.tables.Table;
import lombok.Getter;

import java.util.List;

@Getter
public class Order {
    private List<OrderableDish> orderableDishes;
    private Table table;
    private Waiter waiter;

    public Order(Table table, Waiter waiter, List<OrderableDish> orderableDishes) {
        this.orderableDishes = orderableDishes;
        this.table = table;
        this.waiter = waiter;
    }
}
