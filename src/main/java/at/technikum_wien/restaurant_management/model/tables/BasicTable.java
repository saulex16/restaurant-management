package at.technikum_wien.restaurant_management.model.tables;

import lombok.Getter;

@Getter
public class BasicTable extends Table {

    public BasicTable(long id, String name) {
        super(id, name, TableType.BASIC);
    }
}
