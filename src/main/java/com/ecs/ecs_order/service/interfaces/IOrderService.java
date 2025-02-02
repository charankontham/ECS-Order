package com.ecs.ecs_order.service.interfaces;

import com.ecs.ecs_order.dto.*;

import java.io.File;
import java.util.List;
import java.util.Set;

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

    List<ProductFinalDto> getOrderItemsByCustomerId(Integer customerId);

    Object downloadOrderInvoice(Integer invoiceId);
}
