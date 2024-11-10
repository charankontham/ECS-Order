package com.ecs.ecs_order.controller;

import com.ecs.ecs_order.dto.OrderDto;
import com.ecs.ecs_order.dto.OrderFinalDto;
import com.ecs.ecs_order.dto.OrderItemDto;
import com.ecs.ecs_order.dto.OrderRequest;
import com.ecs.ecs_order.service.interfaces.IOrderService;
import com.ecs.ecs_order.util.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderFinalDto> getOrderById(@PathVariable("id") Integer orderId) {
        OrderFinalDto orderFinalDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderFinalDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderFinalDto>> getAllOrders() {
        List<OrderFinalDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrdersByCustomerId/{id}")
    public ResponseEntity<List<OrderFinalDto>> getAllOrdersByCustomerId(@PathVariable("id") Integer customerId) {
        List<OrderFinalDto> orders = orderService.getAllOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrdersByProductId/{id}")
    public ResponseEntity<List<OrderFinalDto>> getAllOrdersByProductId(@PathVariable("id") Integer productId) {
        List<OrderFinalDto> orders = orderService.getAllOrdersByProductId(productId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrderItemsByProductId/{id}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByProductId(@PathVariable("id") Integer productId) {
        List<OrderItemDto> orderItems = orderService.getOrderItemsByProductId(productId);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping("/existsByProductId/{id}")
    public ResponseEntity<Boolean> existsByProductId(@PathVariable("id") Integer productId) {
        return new ResponseEntity<>(orderService.existsByProductId(productId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody OrderRequest orderRequest) {
        Object response = orderService.addOrder(orderRequest);
        return HelperFunctions.getResponseEntity(response);
    }

    @PutMapping
    public ResponseEntity<?> updateOrder(@RequestBody OrderDto orderDto) {
        Object response = orderService.updateOrder(orderDto);
        return HelperFunctions.getResponseEntity(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Integer orderId) {
        orderService.deleteOrderById(orderId);
        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }

}
