package at.technikum_wien.restaurant_management.repository.interfaces;

import at.technikum_wien.restaurant_management.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    long createRestaurant(Restaurant restaurant);
    Optional<Restaurant> getRestaurant(Long id);
    void deleteRestaurant(Long id);

    long addWaiter(String name);
    long addBasicTable(String name);
    long addVipTable(String name);
}
