package at.technikum_wien.restaurant_management.repository.interfaces;

import at.technikum_wien.restaurant_management.model.Kitchen;
import at.technikum_wien.restaurant_management.model.Menu;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.tables.Table;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    void createRestaurant(Restaurant restaurant);
    Optional<Restaurant> getRestaurant(Long id);
    void deleteRestaurant(Long id);

    void addTables(List<Table> tables);
    void addKitchen(Kitchen kitchen);
}
