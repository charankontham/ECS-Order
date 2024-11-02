package com.ecs.ecs_order.mapper;

import com.ecs.ecs_order.dto.CartItemDto;
import com.ecs.ecs_order.entity.CartItem;

public class CartItemMapper {
    public static CartItemDto mapToCartItemDto(CartItem cartItem) {
        return new CartItemDto(
                cartItem.getCartItemId(),
                cartItem.getCustomerId(),
                cartItem.getProductId(),
                cartItem.getQuantity()
        );
    }
    public static CartItem mapToCartItem(CartItemDto cartItemDto) {
        return new CartItem(
                cartItemDto.getCartItemId(),
                cartItemDto.getCustomerId(),
                cartItemDto.getProductId(),
                cartItemDto.getQuantity()
        );
    }
}
