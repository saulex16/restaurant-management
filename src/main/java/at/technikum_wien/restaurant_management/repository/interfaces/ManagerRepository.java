package at.technikum_wien.restaurant_management.repository.interfaces;

import at.technikum_wien.restaurant_management.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
