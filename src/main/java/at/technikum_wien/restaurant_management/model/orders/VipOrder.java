package at.technikum_wien.restaurant_management.model.orders;

import at.technikum_wien.restaurant_management.model.Waiter;
import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;
import at.technikum_wien.restaurant_management.model.tables.VipTable;

import java.util.List;

public class VipOrder extends Order {

    public VipOrder(VipTable vipTable, Waiter waiter, List<OrderedDish> orderedDishes, OrderState state) {
        super(vipTable, waiter, orderedDishes, state);
    }
}
