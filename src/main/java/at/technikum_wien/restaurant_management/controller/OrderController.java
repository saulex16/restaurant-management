package at.technikum_wien.restaurant_management.controller;

import at.technikum_wien.restaurant_management.dtos.AddDishToOrderDto;
import at.technikum_wien.restaurant_management.dtos.CreateOrderDto;
import at.technikum_wien.restaurant_management.model.bills.Bill;
import at.technikum_wien.restaurant_management.model.orders.Order;
import at.technikum_wien.restaurant_management.service.interfaces.OrderService;
import at.technikum_wien.restaurant_management.utils.Endpoints;
import at.technikum_wien.restaurant_management.vnd_type.VndType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(Endpoints.ORDERS)
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(path = "/")
    public long createOrder(
            @RequestBody CreateOrderDto createOrderDto
    ) {
        return orderService.createOrder(
                createOrderDto.getRestaurantId(),
                createOrderDto.getTableId(),
                createOrderDto.getWaiterId()
        );
    }

    @GetMapping(path = "/{orderId}")
    public Order getOrder(
            @PathVariable long orderId
    ) {
        return orderService
                .getOrderById(orderId)
                .orElse(null);
    }

    @PutMapping(path = "/{orderId}/dishes/")
    public long addDishToOrder(
            @PathVariable long orderId,
            @RequestBody AddDishToOrderDto addDishToOrderDto
    ) {
        return orderService.addDishToOrder(
                orderId,
                addDishToOrderDto.getDishId(),
                addDishToOrderDto.getAddedIngredientIds()
        );
    }

    @PutMapping(path = "/{orderId}", consumes = VndType.ORDER_QUEUE_VND_TYPE)
    public boolean queueOrder(
            @PathVariable long orderId
    ) {
        orderService.queueOrder(orderId);
        return true;
    }

    /*@PutMapping(path = "/{orderId}", consumes = VndType.ORDER_DELIVER_VND_TYPE)
    public boolean deliverOrder(
            @PathVariable long orderId
    ) {
        orderService.deliverOrder(orderId);
        return true;
    }*/

    @PutMapping(path = "/{orderId}", consumes = VndType.ORDER_BILL_VND_TYPE)
    public Bill getOrderBill(
            @PathVariable long orderId
    ) {
        return orderService.getBill(orderId);
    }
}
