package com.ecs.ecs_order.service.interfaces;

import com.ecs.ecs_order.dto.OrderDto;
import com.ecs.ecs_order.dto.OrderFinalDto;
import com.ecs.ecs_order.dto.OrderItemDto;
import com.ecs.ecs_order.dto.OrderRequest;

import java.io.File;
import java.util.List;

public interface IOrderService {
    OrderFinalDto getOrderById(Integer orderId);

    List<OrderFinalDto> getAllOrdersByCustomerId(Integer customerId);

    List<OrderFinalDto> getAllOrdersByProductId(Integer productId);

    Boolean existsByProductId(Integer productId);

    List<OrderFinalDto> getAllOrders();

    Object addOrder(OrderRequest orderRequest);

    Object updateOrder(OrderDto orderDto) throws Exception;

    void deleteOrderById(Integer orderId);

    List<OrderItemDto> getOrderItemsByProductId(Integer productId);

    Object downloadOrderInvoice(Integer invoiceId);
}
