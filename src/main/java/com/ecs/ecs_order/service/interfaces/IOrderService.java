package com.ecs.ecs_order.service.interfaces;

import com.ecs.ecs_order.dto.OrderDto;
import com.ecs.ecs_order.dto.OrderFinalDto;
import com.ecs.ecs_order.dto.OrderRequest;

import java.util.List;

public interface IOrderService {

    OrderFinalDto getOrderById(int orderId);

    List<OrderFinalDto> getAllOrdersByCustomerId(int customerId);

    List<OrderFinalDto> getAllOrdersByProductId(int productId);

    List<OrderFinalDto> getAllOrders();

    Object addOrder(OrderRequest orderRequest);

    Object updateOrder(OrderDto orderDto);

//    Object updateOrder(OrderDto orderDto, boolean forceUpdate);

    void deleteOrderById(int orderId);

}
