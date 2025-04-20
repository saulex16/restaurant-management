package at.technikum_wien.restaurant_management.repository.interfaces;

import at.technikum_wien.restaurant_management.model.dishes.OrderedDish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedDishRepository extends JpaRepository<OrderedDish, Long> {
}
