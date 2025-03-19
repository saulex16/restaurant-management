package at.technikum_wien.restaurant_management.model.bills;

import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;
import at.technikum_wien.restaurant_management.model.orders.Order;
import at.technikum_wien.restaurant_management.model.tables.Table;


public interface BillVisitor {
    void visitTable(Table table);
    void visitOrder(Order order);
}
