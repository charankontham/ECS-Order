package com.ecs.ecs_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartFinalDto {
    private CustomerDto customer;
    private List<CartItemEnrichedDto> cartItems;
}
