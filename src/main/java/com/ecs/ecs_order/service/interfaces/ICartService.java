package com.ecs.ecs_order.service.interfaces;

import com.ecs.ecs_order.dto.CartFinalDto;
import com.ecs.ecs_order.dto.CartDto;

public interface ICartService {

    CartFinalDto getCartByCustomerId(int customerId);

    Object addOrUpdateCartItem(CartDto cartDto);

    boolean deleteCartItem(int cartItemId);
}
