package at.technikum_wien.restaurant_management.model.bills;

public interface Billable {
    void accept(BillVisitor visitor);
}
