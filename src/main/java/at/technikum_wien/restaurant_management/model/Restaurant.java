package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.tables.Table;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Entity
@jakarta.persistence.Table(name = "restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "restaurant_id_generator")
    @SequenceGenerator(name = "restaurant_id_generator", sequenceName = "restaurant_id_seq", allocationSize = 1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "vip_table_price", nullable = true)
    private Double vipTablePrice;


    private List<Table> tables;
    private List<Waiter> waiters;
    private Kitchen kitchen;
    private Manager manager;
    private Menu menu;
    private Warehouse warehouse;
    private Double vipTablePrice;
}
