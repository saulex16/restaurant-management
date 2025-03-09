package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.interfaces.OrderableDish;
import at.technikum_wien.restaurant_management.model.tables.VipTable;

import java.util.List;

public class VipOrder extends Order {

    public VipOrder(VipTable vipTable, Waiter waiter, List<OrderableDish> orderableDishes) {
        super(vipTable, waiter, orderableDishes);
    }
}
