package at.technikum_wien.restaurant_management.controller;

import at.technikum_wien.restaurant_management.dtos.DishDto;
import at.technikum_wien.restaurant_management.model.dishes.Dish;
import at.technikum_wien.restaurant_management.service.interfaces.DishService;
import at.technikum_wien.restaurant_management.utils.Endpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(Endpoints.RESTAURANTS + "/{restaurantId}" + Endpoints.DISHES)
public class DishController {


    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping()
    public ResponseEntity<DishDto> createDish(@PathVariable final Long restaurantId, @RequestBody DishDto dishDto) {
        Dish dish = dishService.createDish(restaurantId, dishDto.getName(),
                dishDto.getBaseIngredientsIds(),
                dishDto.getOptionalIngredientsIds(),
                dishDto.getDurationInMinutes(), dishDto.getMarkup());

        return ResponseEntity.ok(DishDto.fromDish(dish));
    }

    @GetMapping()
    public ResponseEntity<List<DishDto>> getDishes(@PathVariable final Long restaurantId) {

        return dishService.getMenu(restaurantId)
                .map(menu -> {
                    List<DishDto> dishDtos = menu.getDishes()
                            .stream()
                            .map(DishDto::fromDish)
                            .collect(Collectors.toList());
                    return ResponseEntity.ok(dishDtos);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishDto> getDish(@PathVariable final Long restaurantId, @PathVariable final Long id) {
        Optional<Dish> maybeDish = dishService.getDishById(restaurantId, id);

        return maybeDish.map(dish -> ResponseEntity.ok(DishDto.fromDish(dish)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    //TODO: Use Dto fields to update
    @PutMapping("/{id}")
    public ResponseEntity<DishDto> updateDish(@PathVariable final long restaurantId, @PathVariable final Long id,
                                              @RequestBody DishDto dto){
        Optional<Dish> maybeDish = dishService.getDishById(restaurantId, id);

        if (maybeDish.isPresent()) {
            Dish dish = maybeDish.get();
            dish.setDurationInMinutes(dto.getDurationInMinutes());
            dish.setMarkup(dto.getMarkup());
            Dish savedDish = dishService.updateDish(restaurantId, dish);
            return ResponseEntity.ok(DishDto.fromDish(savedDish));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable final Long restaurantId, @PathVariable final Long id) {
        //TODO: Exception mapper should manage the case the dish doesnt exists
        dishService.deleteDish(restaurantId, id);
        return ResponseEntity.noContent().build();
    }
}
