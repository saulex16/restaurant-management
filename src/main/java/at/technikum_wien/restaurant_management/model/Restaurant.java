package at.technikum_wien.restaurant_management.model;

import at.technikum_wien.restaurant_management.model.tables.Table;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Entity
@jakarta.persistence.Table(name = "restaurant")
@Getter
@Setter
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

    @OneToMany(mappedBy = "restaurant")
    private List<Table> tables;

    @OneToMany(mappedBy = "restaurant")
    private List<Waiter> waiters;

    @OneToOne(mappedBy = "restaurant")
    @JoinColumn(name = "kitchen_id", referencedColumnName = "id")
    private Kitchen kitchen;

    @OneToOne(mappedBy = "restaurant")
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    private Manager manager;

    @OneToOne(mappedBy = "restaurant")
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;

    @OneToOne(mappedBy = "restaurant")
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;

    public Restaurant() {}

    public Restaurant(String name, Manager manager, Kitchen kitchen, List<Waiter> waiters, Optional<Double> vipTablePrice) {
        this.name = name;
        this.manager = manager;
        this.kitchen = kitchen;
        this.waiters = waiters;
        this.vipTablePrice = vipTablePrice.orElse(null);
    }
}
