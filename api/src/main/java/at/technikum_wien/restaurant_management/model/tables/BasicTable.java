package at.technikum_wien.restaurant_management.model.tables;

import at.technikum_wien.restaurant_management.model.Restaurant;
import jakarta.persistence.Entity;

@Entity
public class BasicTable extends Table {

    public BasicTable(Restaurant restaurant, String name) {
        super(restaurant, name, TableType.BASIC, 0);
    }

    public BasicTable() {
        super();
    }
}
