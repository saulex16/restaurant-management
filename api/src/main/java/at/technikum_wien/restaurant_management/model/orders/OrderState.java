package at.technikum_wien.restaurant_management.model.orders;

public enum OrderState {
    CREATED,    // The order is created
    QUEUED,     // The waiter send the final order
    PREPARING,  // The order dishes are being prepared
    COOKED,     // The order dishes have been cooked
    DELIVERED,  // The order dishes is delivered to the table
    BILL_DELIVERED,  // The client got the receipt
}
