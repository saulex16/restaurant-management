package at.technikum_wien.restaurant_management.controller;

import at.technikum_wien.restaurant_management.dtos.AddTableDto;
import at.technikum_wien.restaurant_management.dtos.CreateRestaurantDto;
import at.technikum_wien.restaurant_management.dtos.VipTablePriceDto;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.service.interfaces.RestaurantService;
import at.technikum_wien.restaurant_management.vnd_type.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping(path = "/{id}")
    public Restaurant getRestaurant(@PathVariable Long id){
        return restaurantService.getRestaurant(id);
    }

    @PostMapping(path = "/")
    public long createRestaurant(
            @RequestBody CreateRestaurantDto createRestaurantDto
    ) {
        Restaurant restaurant = restaurantService.createRestaurant(
                createRestaurantDto.getName(),
                createRestaurantDto.getManagerName(),
                createRestaurantDto.getKitchenLimit(),
                createRestaurantDto.getWaiterNames(),
                Optional.ofNullable(createRestaurantDto.getVipTablePrice())
        );
        return restaurant.getId();
    }

    @PostMapping(path = "/{id}/waiters/{name}")
    public long addWaiter(
            @PathVariable Long id,
            @PathVariable String name) {
        long waiterId = restaurantService.addWaiter(id, name);
        return waiterId;
    }

    @PutMapping(path = "/{id}/tables/price", consumes = VndType.VIP_TABLE_VND_TYPE)
    public void setVipTablePrice(
            @PathVariable Long id,
            @RequestBody VipTablePriceDto vipTablePriceDto) {
        restaurantService.setVipTablePrice(id, vipTablePriceDto.getVipTablePrice());
    }

    @PostMapping(path = "/{id}/tables", consumes = VndType.BASIC_TABLE_VND_TYPE)
    public long createBasicTable(
            @PathVariable Long id,
            @RequestBody AddTableDto addTableDto) {
        long tableId = restaurantService.addBasicTable(id, addTableDto.getTableName());
        return tableId;
    }

    @PostMapping(path = "/{id}/tables", consumes = VndType.VIP_TABLE_VND_TYPE)
    public long createVipTable(
            @PathVariable Long id,
            @RequestBody AddTableDto addTableDto) {
        long tableId = restaurantService.addVipTable(id, addTableDto.getTableName());
        return tableId;
    }

}
