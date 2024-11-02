package com.ecs.ecs_order.mapper;

import com.ecs.ecs_order.dto.CartFinalDto;
import com.ecs.ecs_order.dto.CartItemDto;
import com.ecs.ecs_order.dto.CartItemEnrichedDto;
import com.ecs.ecs_order.exception.ResourceNotFoundException;
import com.ecs.ecs_order.feign.CustomerService;
import com.ecs.ecs_order.feign.ProductService;
import org.springframework.http.HttpStatus;
import java.util.List;

public class CartMapper {

    public static CartFinalDto mapToCartFinalDto(Integer customerId,
                                                 List<CartItemDto> cartItemDtoList,
                                                 CustomerService customerService,
                                                 ProductService productService) {
        if(customerService.getCustomerById(customerId).getStatusCode() != HttpStatus.OK){
            throw new ResourceNotFoundException("Customer not found");
        }
        return new CartFinalDto(
                customerService.getCustomerById(customerId).getBody(),
                cartItemDtoList.stream().
                        map(cartItemDto -> mapToCartItemEnrichedDto(cartItemDto,productService)).toList()
        );
    }

    public static CartItemEnrichedDto mapToCartItemEnrichedDto(CartItemDto cartItemDto, ProductService productService){
        return new CartItemEnrichedDto(
                cartItemDto.getCartItemId(),
                ProductMapper.getProductFinalDtoListWithCartItem(cartItemDto, productService)
        );
    }
}
