package com.ecs.ecs_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Integer orderItemId;
    private Integer orderId;
    private Integer productId;
    private Integer quantity;
    private Float productPrice;
}
