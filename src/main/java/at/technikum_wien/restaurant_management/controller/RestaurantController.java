package at.technikum_wien.restaurant_management.controller;

import at.technikum_wien.restaurant_management.dtos.CreateRestaurantDto;
import at.technikum_wien.restaurant_management.model.Restaurant;
import at.technikum_wien.restaurant_management.service.interfaces.RestaurantService;
import at.technikum_wien.restaurant_management.vnd_type.BasicTableVndType;
import at.technikum_wien.restaurant_management.vnd_type.VipTableVndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final VipTableVndType vipTableVndType = new VipTableVndType();
    private final basicTableVndType basicTableVndType = new BasicTableVndType();

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping(path = "/{id}")
    public Restaurant getRestaurant(@PathVariable Long id){

    }

    @PostMapping(path = "/")
    public void createRestaurant(
            @RequestBody CreateRestaurantDto createRestaurantDto
    ) {

        // strategy = strategyDispatcher.getStrategy(restaurantForm.getType())
        // strategy.addTable(restaurantForm)

        // service.createVipTable(..., form.getPrice())
        // tableForm -> (vipTableForm, basicTableForm)

        // ValidationUtils.validate(dto, PostGroup.class);
    }


    @PostMapping(path = "/tables/{name}", consumes = ba

}
