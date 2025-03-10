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

    @PostMapping(path = "/waiters/{name}")
    public long addWaiter(@PathVariable String name) {
        long waiterId = restaurantService.addWaiter(name);
        return waiterId;
    }

    @PutMapping(path = "/tables/price", consumes = VndType.VIP_TABLE_VND_TYPE)
    public void setVipTablePrice(@RequestBody VipTablePriceDto vipTablePriceDto) {
        restaurantService.setVipTablePrice(vipTablePriceDto.getVipTablePrice());
    }

    @PostMapping(path = "/tables", consumes = VndType.BASIC_TABLE_VND_TYPE)
    public long createBasicTable(@RequestBody AddTableDto addTableDto) {
        long tableId = restaurantService.addBasicTable(addTableDto.getTableName());
        return tableId;
    }

    @PostMapping(path = "/tables", consumes = VndType.VIP_TABLE_VND_TYPE)
    public long createVipTable(@RequestBody AddTableDto addTableDto) {
        long tableId = restaurantService.addVipTable(addTableDto.getTableName());
        return tableId;
    }

}
