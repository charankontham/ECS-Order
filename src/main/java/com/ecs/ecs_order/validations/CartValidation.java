package com.ecs.ecs_order.validations;

import com.ecs.ecs_order.dto.CartDto;
import com.ecs.ecs_order.dto.CartItemDto;
import com.ecs.ecs_order.feign.CustomerService;
import com.ecs.ecs_order.feign.ProductService;
import com.ecs.ecs_order.util.HelperFunctions;
import java.util.List;
import java.util.Objects;

public class CartValidation {
    public static boolean validateCartRequestSchema(CartDto cartDto) {
        return Objects.nonNull(cartDto) &&
                Objects.nonNull(cartDto.getCustomerId()) &&
                !cartDto.getCartItems().isEmpty() &&
                HelperFunctions.checkZeroQuantities(
                        cartDto.getCartItems().
                                stream().
                                map(CartItemDto::getQuantity).
                                toList()) &&
                checkDuplicateCartItem(cartDto.getCartItems()) &&
                validateCartItemsList(cartDto.getCartItems()) &&
                checkCustomerIntegrity(cartDto);
    }

    public static boolean validateCartItemRequestSchema(CartItemDto cartItemDto) {
        return Objects.nonNull(cartItemDto.getProductId()) &&
                Objects.nonNull(cartItemDto.getQuantity());
    }

    public static boolean checkDuplicateCartItem(List<CartItemDto> cartItemDtoList) {
        List<Integer> productIds = cartItemDtoList.stream().map(CartItemDto::getProductId).toList();
        return HelperFunctions.checkDuplicatesInList(productIds);
    }

    public static boolean validateCartItemsList(List<CartItemDto> cartItemDtoList) {
        return cartItemDtoList.stream().
                filter(CartValidation::validateCartItemRequestSchema).
                toList().size() == cartItemDtoList.size();
    }

    public static Object validateCustomerAndCartItems(
            CartDto cartDto,
            ProductService productService,
            CustomerService customerService
    ) {
        List<Integer> productIds = cartDto.getCartItems().stream().map(CartItemDto::getProductId).toList();
        List<Integer> quantityList = cartDto.getCartItems().stream().map(CartItemDto::getQuantity).toList();
        return HelperFunctions.validateCustomerAndProductQuantities(
                cartDto.getCustomerId(),
                productIds,
                quantityList,
                productService,
                customerService
        );
    }

    public static boolean checkCustomerIntegrity(CartDto cartDto) {
        List<CartItemDto> list = cartDto.getCartItems().
                stream().filter(x -> Objects.equals(x.getCustomerId(), cartDto.getCustomerId())).toList();
        return list.size() == cartDto.getCartItems().size();
    }
}
