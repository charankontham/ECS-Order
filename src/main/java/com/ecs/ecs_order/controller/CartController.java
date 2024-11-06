package com.ecs.ecs_order.controller;

import com.ecs.ecs_order.dto.CartDto;
import com.ecs.ecs_order.service.interfaces.ICartService;
import com.ecs.ecs_order.util.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private ICartService ICartService;

    @GetMapping("/getCartByCustomerId/{id}")
    public ResponseEntity<?> getCartByCustomerId(@PathVariable("id") Integer customerId) {
        return ResponseEntity.ok(ICartService.getCartByCustomerId(customerId));
    }

    @GetMapping("/existsByProductId/{id}")
    public ResponseEntity<Boolean> existsByProductId(@PathVariable("id") Integer productId) {
        return ResponseEntity.ok(ICartService.isCartItemsExistsByProductId(productId));
    }

    @PostMapping
    public ResponseEntity<?> addCart(@RequestBody CartDto cartDto) {
        Object response = ICartService.addOrUpdateCartItem(cartDto);
        ResponseEntity<?> responseEntity = HelperFunctions.getResponseEntity(response);
        if(responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            return ResponseEntity.status(HttpStatus.CREATED).body(responseEntity.getBody());
        }
        return responseEntity;
    }

    @PutMapping
    public ResponseEntity<?> updateCart(@RequestBody CartDto cartDto) {
        Object response = ICartService.addOrUpdateCartItem(cartDto);
        return HelperFunctions.getResponseEntity(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("id") Integer cartItemId) {
        boolean response = ICartService.deleteCartItem(cartItemId);
        if(response){
            return ResponseEntity.status(HttpStatus.OK).body("CartItem successfully deleted!");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CartItem not found!");
        }
    }
}
