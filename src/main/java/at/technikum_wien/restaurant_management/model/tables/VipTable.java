package at.technikum_wien.restaurant_management.model.tables;

public class VipTable extends Table {

    private double price;

    public VipTable(String name, double price) {
        super(name, TableType.VIP, price);
    }
}
