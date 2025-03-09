package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.tables.Table;
import lombok.Getter;

import java.util.List;

@Getter
public class Restaurant {
    private long id;
    private String name;
    private List<Table> tables;
    private List<Waiter> waiters;
    private Kitchen kitchen;
    private Manager manager;
    private Menu menu;
    private Warehouse warehouse;
    private Double vipTablePrice;
}
