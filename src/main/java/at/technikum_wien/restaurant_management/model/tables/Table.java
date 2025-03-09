package at.technikum_wien.restaurant_management.model.tables;

import lombok.Getter;

@Getter
public class Table {
    private long id;
    private String name;
    private TableType tableType;

    public Table(long id, String name, TableType tableType) {
        this.id = id;
        this.name = name;
        this.tableType = tableType;
    }
}
