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
public class OrderRequest {
    private OrderDto orderDetails;
    private List<OrderItemDto> orderItems;
}
