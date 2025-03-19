package at.technikum_wien.restaurant_management.model.notifications;

public enum NotificationType {
    OUT_OF_STOCK, // When the quantity of an ingredient is 0
    ADDED_STOCK, // When the manager adds elements to an ingredient
    NEW_ORDER, // When a waiter creates a new order
    ORDER_READY // When an order is ready to be served
}
