package at.technikum_wien.restaurant_management.model;

import lombok.Data;


import java.util.List;

@Data
public class Menu {

    private List<Dish> dishes;

    private Restaurant restaurant;

    public Menu(List<Dish> dishes, Restaurant restaurant) {
        this.dishes = dishes;
        this.restaurant = restaurant;
    }
}
