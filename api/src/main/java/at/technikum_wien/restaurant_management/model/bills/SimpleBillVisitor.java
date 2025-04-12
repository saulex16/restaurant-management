package at.technikum_wien.restaurant_management.model.bills;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;
import at.technikum_wien.restaurant_management.model.orders.Order;
import at.technikum_wien.restaurant_management.model.tables.Table;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleBillVisitor implements BillVisitor {

    private final Map<String, Double> billItems;

    public SimpleBillVisitor() {
        billItems = new HashMap<>();
    }

    @Override
    public void visitTable(Table table) {
        double price = table.getPrice();
        if (price > 0) {
            String itemName = "Table " + table.getName();
            billItems.put(itemName, price);
        }
    }

    @Override
    public void visitOrder(Order order) {
        List<OrderedDish> orderedDishes = order.getOrderedDishes();
        for (OrderedDish orderedDish : orderedDishes) {
            double price = orderedDish.getTotalPrice();
            StringBuilder itemNameBuilder = new StringBuilder();
            itemNameBuilder
                    .append(orderedDish.getDish().getName())
                    .append(" (");
            List<Ingredient> addedIngredients = orderedDish.getAddedIngredients();
            for (Ingredient ingredient : addedIngredients) {
                itemNameBuilder
                        .append(ingredient.getName())
                        .append(", ");
            }
            itemNameBuilder.delete(itemNameBuilder.length() - 2, itemNameBuilder.length());
            itemNameBuilder.append(")");
            String itemName = itemNameBuilder.toString();
            billItems.put(itemName, price);
        }
    }

    public Bill getBill() {
        LocalDateTime dateTime = LocalDateTime.now();
        return new Bill(dateTime, billItems);
    }
}
