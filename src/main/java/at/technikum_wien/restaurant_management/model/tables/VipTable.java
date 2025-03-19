package at.technikum_wien.restaurant_management.model.tables;

public class VipTable extends Table {

    public VipTable(String name, double price, boolean isOccupied) {
        super(name, TableType.VIP, price, isOccupied);
    }
}
