package at.technikum_wien.restaurant_management.dtos;

import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Warehouse;
import at.technikum_wien.restaurant_management.model.stock.Stock;

public class StockDto {
    private long id;

    private long ingredient;

    private long quantity;

    private long warehouse;

    public static StockDto fromStock(Stock stock){
        StockDto dto = new StockDto();
        dto.setId(stock.getId());
        dto.setIngredient(stock.getIngredient().getId());
        dto.setQuantity(stock.getQuantity());
        dto.setWarehouse(stock.getWarehouse().getId());

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIngredient() {
        return ingredient;
    }

    public void setIngredient(long ingredient) {
        this.ingredient = ingredient;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public long getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(long warehouse) {
        this.warehouse = warehouse;
    }
}
