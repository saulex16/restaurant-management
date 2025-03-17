package at.technikum_wien.restaurant_management.service;

import at.technikum_wien.restaurant_management.model.Kitchen;
import at.technikum_wien.restaurant_management.model.Manager;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.Waiter;
import at.technikum_wien.restaurant_management.repository.interfaces.KitchenRepository;
import at.technikum_wien.restaurant_management.repository.interfaces.ManagerRepository;
import at.technikum_wien.restaurant_management.repository.interfaces.RestaurantRepository;
import at.technikum_wien.restaurant_management.repository.interfaces.WaiterRepository;
import at.technikum_wien.restaurant_management.service.interfaces.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ManagerRepository managerRepository;
    private final KitchenRepository kitchenRepository;
    private final WaiterRepository waiterRepository;

    @Autowired
    public RestaurantServiceImpl(
            RestaurantRepository restaurantRepository,
            ManagerRepository managerRepository,
            KitchenRepository kitchenRepository,
            WaiterRepository waiterRepository
    ) {
        this.restaurantRepository = restaurantRepository;
        this.managerRepository = managerRepository;
        this.kitchenRepository = kitchenRepository;
        this.waiterRepository = waiterRepository;
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
        return kitchenRepository.save(new Kitchen(kitchenLimit));
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
        List<Waiter> waiters = createAndGetWaiters(waiterNames);
        Restaurant restaurant = new Restaurant(name, manager, kitchen, waiters, vipTablePrice);
        return restaurantRepository.save(restaurant);
    }

    @Override
    public long addWaiter(String waiterName) {
        Optional<Waiter> maybeWaiter = waiterRepository.findByName(waiterName);
        if (maybeWaiter.isPresent()) {
            throw new IllegalArgumentException("Waiter already exists");
        }
        Waiter waiter = new Waiter(waiterName);
        waiterRepository.save(waiter);
    }

    @Override
    public void setVipTablePrice(Double vipTablePrice) {

    }

    @Override
    public long addBasicTable(String tableName) {
        return 0;
    }

    @Override
    public long addVipTable(String tableName) {
        return 0;
    }
}
