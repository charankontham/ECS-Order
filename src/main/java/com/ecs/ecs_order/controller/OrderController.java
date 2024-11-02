package com.ecs.ecs_order.controller;

import com.ecs.ecs_order.dto.OrderDto;
import com.ecs.ecs_order.dto.OrderFinalDto;
import com.ecs.ecs_order.dto.OrderRequest;
import com.ecs.ecs_order.service.interfaces.IOrderService;
import com.ecs.ecs_order.util.Constants;
import com.ecs.ecs_order.util.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderFinalDto> getOrderById(@PathVariable("id") int orderId){
        OrderFinalDto orderFinalDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderFinalDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderFinalDto>> getAllOrders(){
        List<OrderFinalDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrdersByCustomerId/{id}")
    public ResponseEntity<List<OrderFinalDto>> getAllOrdersByCustomerId(@PathVariable("id") int customerId){
        List<OrderFinalDto> orders = orderService.getAllOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrdersByProductId/{id}")
    public ResponseEntity<List<OrderFinalDto>> getAllOrdersByProductId(@PathVariable("id") int productId){
        List<OrderFinalDto> orders = orderService.getAllOrdersByProductId(productId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody OrderRequest orderRequest){
        Object response = orderService.addOrder(orderRequest);
        if(Objects.equals(response, HttpStatus.CONFLICT)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Order already exists with Id : " + orderRequest.getOrderDetails().getOrderId());
        }
        return HelperFunctions.getResponseEntity(response);
    }

    @PutMapping
    public ResponseEntity<?> updateOrder(@RequestBody OrderDto orderDto){
        Object response = orderService.updateOrder(orderDto);
        if(Objects.equals(response, Constants.OrderNotFound)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Not Found with Id : " + orderDto.getOrderId());
        }
        return HelperFunctions.getResponseEntity(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") int orderId){
        orderService.deleteOrderById(orderId);
        return new ResponseEntity<>("Order Deleted Successfully", HttpStatus.OK);
    }

}
