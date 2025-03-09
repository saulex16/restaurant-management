package at.technikum_wien.restaurant_management.model.tables;

public class VipTable extends Table {

    private final double price;

    public VipTable(long id, String name, double price) {
        super(id, name, TableType.VIP);
        this.price = price;
    }
}
