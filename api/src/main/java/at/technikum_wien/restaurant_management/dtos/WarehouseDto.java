package at.technikum_wien.restaurant_management.dtos;

import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.Warehouse;
import at.technikum_wien.restaurant_management.model.stock.Stock;

import java.util.List;
import java.util.stream.Collectors;

public class WarehouseDto {

    private long id;

    private List<Long> stock;

    private Long restaurant;

    public static WarehouseDto fromWarehouse(Warehouse warehouse){
        List<Long> stockIds = warehouse.getStock().stream().map(Stock::getId).toList();
        WarehouseDto dto = new WarehouseDto();
        dto.setId(warehouse.getId());
        dto.setRestaurant(warehouse.getRestaurant().getId());
        dto.setStock(stockIds);

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Long> getStock() {
        return stock;
    }

    public void setStock(List<Long> stock) {
        this.stock = stock;
    }

    public long getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(long restaurant) {
        this.restaurant = restaurant;
    }
}
