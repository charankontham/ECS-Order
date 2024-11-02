package com.ecs.ecs_order.mapper;

import com.ecs.ecs_order.dto.OrderItemDto;
import com.ecs.ecs_order.entity.OrderItem;

public class OrderItemMapper {
    public static OrderItemDto mapToOrderItemDto(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getOrderItemId(),
                orderItem.getOrderId(),
                orderItem.getProductId(),
                orderItem.getQuantity(),
                orderItem.getProductPrice()
        );
    }
    public static OrderItem mapToOrderItem(OrderItemDto orderItemDto) {
        return new OrderItem(
                orderItemDto.getOrderItemId(),
                orderItemDto.getOrderId(),
                orderItemDto.getProductId(),
                orderItemDto.getQuantity(),
                orderItemDto.getProductPrice()
        );
    }
}
