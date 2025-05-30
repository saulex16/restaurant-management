package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Kitchen;
import at.technikum_wien.restaurant_management.model.Manager;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.Waiter;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.model.tables.BasicTable;
import at.technikum_wien.restaurant_management.model.tables.Table;
import at.technikum_wien.restaurant_management.model.tables.TableType;
import at.technikum_wien.restaurant_management.model.tables.VipTable;
import at.technikum_wien.restaurant_management.repository.interfaces.*;
import at.technikum_wien.restaurant_management.service.interfaces.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ManagerRepository managerRepository;
    private final KitchenRepository kitchenRepository;
    private final WaiterRepository waiterRepository;
    private final TableRepository tableRepository;

    @Autowired
    public RestaurantServiceImpl(
            RestaurantRepository restaurantRepository,
            ManagerRepository managerRepository,
            KitchenRepository kitchenRepository,
            WaiterRepository waiterRepository,
            TableRepository tableRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.managerRepository = managerRepository;
        this.kitchenRepository = kitchenRepository;
        this.waiterRepository = waiterRepository;
        this.tableRepository = tableRepository;
    }

    @Override
    public Restaurant getRestaurant(long id) {
        return restaurantRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
    }

    private Manager createAndGetManager(String managerName) {
        return managerRepository.save(new Manager(managerName));
    }

    private Kitchen createAndGetKitchen(int kitchenLimit) {
        Kitchen kitchen = new Kitchen(kitchenLimit);
        kitchen.setCurrentDishes(0);
        return kitchenRepository.save(kitchen);
    }

    private Waiter createAndGetWaiter(String waiterName) {
        return waiterRepository.save(new Waiter(waiterName));
    }

    private List<Waiter> createAndGetWaiters(List<String> waiterNames) {
        return waiterNames.stream()
                .map(this::createAndGetWaiter)
                .toList();
    }

    @Override
    public Restaurant createRestaurant(String name, String managerName, int kitchenLimit, List<String> waiterNames, Optional<Double> vipTablePrice) {
        Manager manager = createAndGetManager(managerName);
        Kitchen kitchen = createAndGetKitchen(kitchenLimit);

        Restaurant restaurant = new Restaurant(name, manager, kitchen, new ArrayList<>(), vipTablePrice);
        restaurant = restaurantRepository.save(restaurant);
        Restaurant finalRestaurant = restaurant;
        waiterNames.forEach(wName -> this.addWaiter(finalRestaurant.getId(), wName));

        return finalRestaurant;
    }

    @Override
    public long addWaiter(final long restaurantId, final String waiterName) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        Optional<Waiter> maybeWaiter = waiterRepository.findByName(waiterName);
        if (maybeWaiter.isPresent()) {
            throw new IllegalArgumentException("Waiter already exists");
        }
        Waiter waiter = new Waiter(waiterName);
        waiter.setRestaurant(restaurant);
        Waiter savedWaiter = waiterRepository.save(waiter);
        restaurant.getWaiters().add(waiter);
        restaurantRepository.save(restaurant);
        return savedWaiter.getId();
    }

    @Override
    public void setVipTablePrice(final long restaurantId, final Double vipTablePrice) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        restaurant.setVipTablePrice(vipTablePrice);
        restaurantRepository.save(restaurant);
    }

    private Table addTable(final Restaurant restaurant, final Table table) {
        Optional<Table> maybeTable = restaurant.getTables().stream()
                .filter(t -> t.getName().equals(table.getName()))
                .findAny();
        if (maybeTable.isPresent()) {
            throw new IllegalArgumentException("Table already exists");
        }
        Table savedTable = tableRepository.save(table);
        restaurant.getTables().add(table);
        restaurantRepository.save(restaurant);
        return savedTable;
    }

    @Override
    public long addBasicTable(final long restaurantId, final String tableName) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        Table newTable = new BasicTable(restaurant, tableName);
        return addTable(restaurant, newTable).getId();
    }

    @Override
    public long addVipTable(final long restaurantId, String tableName) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        Double vipTablePrice = restaurant.getVipTablePrice();
        if (vipTablePrice == null) {
            throw new IllegalArgumentException("Vip table price not set");
        }
        Table newTable = new VipTable(restaurant, tableName, vipTablePrice);
        return addTable(restaurant, newTable).getId();
    }

    @Override
    public List<Stock> getAllNonEmptyStocksByRestaurantId(long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new IllegalArgumentException("Restaurant not found"));
        List<Stock> stocks = restaurant.getWarehouse().getStock();
        return stocks.stream()
                .filter(stock -> stock.getQuantity() > 0)
                .toList();
    }
}
