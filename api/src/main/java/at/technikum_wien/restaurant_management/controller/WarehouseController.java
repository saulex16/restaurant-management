package at.technikum_wien.restaurant_management.controller;

import at.technikum_wien.restaurant_management.dtos.StockDto;
import at.technikum_wien.restaurant_management.dtos.WarehouseDto;
import at.technikum_wien.restaurant_management.model.Ingredient;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.model.Warehouse;
import at.technikum_wien.restaurant_management.model.stock.Stock;
import at.technikum_wien.restaurant_management.service.interfaces.IngredientService;
import at.technikum_wien.restaurant_management.service.interfaces.RestaurantService;
import at.technikum_wien.restaurant_management.service.interfaces.WarehouseService;
import at.technikum_wien.restaurant_management.utils.Endpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(Endpoints.RESTAURANTS + "/{restaurantId}" + Endpoints.WAREHOUSE)
public class WarehouseController {

    private final WarehouseService warehouseService;

    private final RestaurantService restaurantService;

    private final IngredientService ingredientService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService, RestaurantService restaurantService, IngredientService ingredientService) {
        this.warehouseService = warehouseService;
        this.restaurantService = restaurantService;
        this.ingredientService = ingredientService;
    }

    @PostMapping
    public ResponseEntity<WarehouseDto> createWarehouse(@PathVariable Long restaurantId) {

        Restaurant restaurant = restaurantService.getRestaurant(restaurantId);
        Warehouse warehouse = warehouseService.createWarehouse(restaurant);
        return ResponseEntity.ok(WarehouseDto.fromWarehouse(warehouse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDto> getWarehouse(@PathVariable Long id, @PathVariable Long restaurantId) {
        Optional<Warehouse> warehouseOptional = warehouseService.getWarehouse(id);
        if (warehouseOptional.isPresent()) {
            Warehouse warehouse = warehouseOptional.get();
            return ResponseEntity.ok(WarehouseDto.fromWarehouse(warehouse));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{warehouseId}" + Endpoints.STOCK)
    public ResponseEntity<StockDto> createStock(@PathVariable Long warehouseId, @PathVariable Long restaurantId, @RequestBody StockDto stockDto){
        Optional<Ingredient>  maybeIngredient = ingredientService.getIngredientById(stockDto.getIngredient());
        if(maybeIngredient.isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        Stock stock = warehouseService.createStockForWarehouse(maybeIngredient.get(), stockDto.getQuantity(), warehouseId);
        return ResponseEntity.ok(StockDto.fromStock(stock));
    }

    @GetMapping("/{warehouseId}" + Endpoints.STOCK + "/{stockId}")
    public ResponseEntity<StockDto> getStock(@PathVariable Long stockId, @PathVariable Long warehouseId, @PathVariable Long restaurantId){
        Optional<Stock> maybeStock = warehouseService.getStock(stockId);
        return maybeStock.map(stock -> ResponseEntity.ok(StockDto.fromStock(stock)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PutMapping("/{warehouseId}" + Endpoints.STOCK + "/{stockId}")
    public ResponseEntity<StockDto> addStock(@PathVariable Long stockId, @PathVariable Long warehouseId,
                                         @PathVariable Long restaurantId, @RequestBody StockDto stockDto){
        Stock stock = warehouseService.addStock(stockId, stockDto.getQuantity());
        return ResponseEntity.ok(StockDto.fromStock(stock));
    }
}
