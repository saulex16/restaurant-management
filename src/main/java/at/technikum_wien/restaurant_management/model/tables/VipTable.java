package at.technikum_wien.restaurant_management.model.tables;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class VipTable extends Table {

    private double price;

    public VipTable() {}

    public VipTable(String name, double price) {
        super(name, TableType.VIP);
        this.price = price;
    }
}
