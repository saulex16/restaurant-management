package at.technikum_wien.restaurant_management.controller;

import at.technikum_wien.restaurant_management.service.interfaces.OrderService;
import at.technikum_wien.restaurant_management.utils.Endpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(Endpoints.ORDERS)
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

}
