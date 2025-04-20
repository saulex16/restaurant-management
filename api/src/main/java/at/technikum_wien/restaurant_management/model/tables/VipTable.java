package at.technikum_wien.restaurant_management.model.tables;

import at.technikum_wien.restaurant_management.model.Restaurant;
import jakarta.persistence.Entity;

@Entity
public class VipTable extends Table {

    public VipTable(Restaurant restaurant, String name, double price) {
        super(restaurant, name, TableType.VIP, price);
    }

    public VipTable() {
        super();
    }
}
