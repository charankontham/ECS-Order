package com.ecs.ecs_order.mapper;

import com.ecs.ecs_order.dto.OrderDto;
import com.ecs.ecs_order.dto.OrderFinalDto;
import com.ecs.ecs_order.dto.OrderItemDto;
import com.ecs.ecs_order.entity.Order;
import com.ecs.ecs_order.feign.CustomerService;
import com.ecs.ecs_order.feign.ProductService;
import com.ecs.ecs_order.util.HelperFunctions;

import java.util.List;

public class OrderMapper {
    public static OrderDto toOrderDto(Order order) {
        return new OrderDto(
                order.getOrderId(),
                order.getCustomerId(),
                order.getAddressId(),
                order.getPaymentType(),
                order.getPaymentStatus(),
                order.getShippingFee(),
                order.getOrderDate(),
                order.getDeliveryDate(),
                order.getShippingStatus()
        );
    }

    public static Order toOrder(OrderDto orderDto, List<OrderItemDto> orderItems) {
        return new Order(
                orderDto.getOrderId(),
                orderDto.getCustomerId(),
                orderDto.getAddressId(),
                orderDto.getPaymentType(),
                orderDto.getPaymentStatus(),
                HelperFunctions.calculateSubTotalPrice(orderItems),
                orderDto.getShippingFee(),
                HelperFunctions.calculateTotalTax(orderItems),
                HelperFunctions.calculateTotalPrice(orderItems,orderDto.getShippingFee()),
                orderDto.getOrderDate(),
                orderDto.getDeliveryDate(),
                orderDto.getShippingStatus()
        );
    }

    public static OrderFinalDto toOrderFinalDto(
            Order order,
            List<OrderItemDto> orderItemDtoList,
            CustomerService customerService,
            ProductService productService) {
        return new OrderFinalDto(
                order.getOrderId(),
                customerService.getCustomerById(order.getCustomerId()).getBody(),
                customerService.getAddressById(order.getAddressId()).getBody(),
                ProductMapper.getProductFinalDtoListWithOrderItems(orderItemDtoList, productService),
                HelperFunctions.calculateSubTotalPrice(orderItemDtoList),
                order.getShippingFee(),
                HelperFunctions.calculateTotalTax(orderItemDtoList),
                HelperFunctions.calculateTotalPrice(orderItemDtoList, order.getShippingFee()),
                order.getOrderDate(),
                order.getDeliveryDate(),
                order.getShippingStatus(),
                order.getPaymentType(),
                order.getPaymentStatus()
        );
    }
}
