package com.ecs.ecs_order.service.interfaces;

import com.ecs.ecs_order.dto.CartFinalDto;
import com.ecs.ecs_order.dto.CartDto;

public interface ICartService {
    CartFinalDto getCartByCustomerId(Integer customerId);

    Object addOrUpdateCartItem(CartDto cartDto);

    boolean deleteCartItem(Integer cartItemId);

    boolean isCartItemsExistsByProductId(Integer productId);
}
