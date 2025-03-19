package at.technikum_wien.restaurant_management.model.tables;


public class BasicTable extends Table {

    public BasicTable(String name, boolean isOccupied) {
        super(name, TableType.BASIC, 0, isOccupied);
    }
}
