package at.technikum_wien.restaurant_management.repository;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.Warehouse;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.repository.interfaces.WarehouseRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class WarehouseRepositoryImpl implements WarehouseRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Warehouse createWarehouse(Restaurant restaurant) {
        Warehouse warehouse = new Warehouse();
        warehouse.setRestaurant(restaurant);
        warehouse.setStock(new ArrayList<>());
        entityManager.persist(warehouse);

        return warehouse;
    }

    @Override
    public Optional<Warehouse> getWarehouse(Long id) {
        TypedQuery<Warehouse> query = entityManager.createQuery("SELECT w FROM Warehouse w WHERE w.id=:id"
                , Warehouse.class);
        query.setParameter("id", id);

        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public Stock createStockForWarehouse(Ingredient ingredient, Long quantity, Long warehouseId) {
        Optional<Warehouse> maybeWarehouse = Optional.ofNullable(entityManager.find(Warehouse.class, warehouseId));
        if(maybeWarehouse.isEmpty()) {
            throw new IllegalArgumentException("Warehouse with ID " + warehouseId + " not found");
        }

        Warehouse warehouse = maybeWarehouse.get();
        Stock stock = new Stock(ingredient,quantity);

        warehouse.getStock().add(stock);
        entityManager.persist(warehouse);

        return stock;
    }

    @Override
    public Stock updateStock(Stock stock) {
        Optional<Stock> maybeStock = Optional.ofNullable(entityManager.find(Stock.class, stock.getId()));
        if(maybeStock.isEmpty()) {
            throw new IllegalArgumentException("Stock with ID " + stock.getId() + " not found");
        }

        Stock stockToUpdate = maybeStock.get();
        stockToUpdate.setQuantity(stock.getQuantity());
        entityManager.merge(stockToUpdate);

        return stockToUpdate;
    }

    @Override
    public Optional<Stock> getStock(Long id) {
        TypedQuery<Stock> query = entityManager.createQuery("SELECT s FROM Stock s WHERE s.id=:id"
                , Stock.class);
        query.setParameter("id", id);

        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Stock> getStockByDish(Dish dish) {
       TypedQuery<Stock> query = entityManager.createQuery("SELECT s FROM Stock s WHERE s.ingredient.id IN :ids AND s.quantity > 0", Stock.class);
       List<Long> ingredientsIds = dish.getBaseIngredients().stream().map(i->i.getStock().getId()).toList();
       query.setParameter("ids", ingredientsIds);

       return query.getResultList();
    }
}
