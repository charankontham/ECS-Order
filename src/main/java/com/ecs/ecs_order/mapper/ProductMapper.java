package com.ecs.ecs_order.mapper;

import com.ecs.ecs_order.dto.CartItemDto;
import com.ecs.ecs_order.dto.OrderItemDto;
import com.ecs.ecs_order.dto.ProductFinalDto;
import com.ecs.ecs_order.feign.ProductService;
import com.ecs.ecs_order.util.HelperFunctions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ProductMapper {

    public static List<ProductFinalDto> getProductFinalDtoListWithOrderItems(
            List<OrderItemDto> orderItemDtoList,
            ProductService productService) {
        int count = 0;
        List<Integer> productIds = orderItemDtoList.stream().map(OrderItemDto::getProductId).toList();
        List<ProductFinalDto> productFinalDtoList =  HelperFunctions.getProductFinalDtoList(productIds, productService);
        for(ProductFinalDto productFinalDto : productFinalDtoList) {
            productFinalDto.setProductQuantity(orderItemDtoList.get(count).getQuantity());
            productFinalDto.setProductPrice(orderItemDtoList.get(count++).getProductPrice());
        }
        return productFinalDtoList;
    }

    public static ProductFinalDto getProductFinalDtoListWithCartItem(
            CartItemDto cartItemDto,
            ProductService productService) {
        List<ProductFinalDto> productFinalDtoList =  HelperFunctions.
                getProductFinalDtoList(Collections.singletonList(cartItemDto.getProductId()), productService);
        productFinalDtoList.get(0).setProductQuantity(cartItemDto.getQuantity());
        return productFinalDtoList.get(0);
    }
}
