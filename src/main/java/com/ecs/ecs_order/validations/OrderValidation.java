package com.ecs.ecs_order.validations;

import com.ecs.ecs_order.dto.OrderDto;
import com.ecs.ecs_order.dto.OrderItemDto;
import com.ecs.ecs_order.dto.OrderRequest;
import com.ecs.ecs_order.feign.CustomerService;
import com.ecs.ecs_order.feign.ProductService;
import com.ecs.ecs_order.util.HelperFunctions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class OrderValidation {
    public static boolean validateOrderRequestSchema(OrderRequest orderRequest) {
        return Objects.nonNull(orderRequest) &&
                Objects.nonNull(orderRequest.getOrderItems()) &&
                !orderRequest.getOrderItems().isEmpty() &&
                validateOrderDtoSchema(orderRequest.getOrderDetails()) &&
                HelperFunctions.checkZeroQuantities(
                        orderRequest.getOrderItems().stream().map(OrderItemDto::getQuantity).toList()) &&
                checkDuplicateOrderItem(orderRequest.getOrderItems()) &&
                validateOrderItemsList(orderRequest.getOrderItems()) &&
                BasicValidation.stringValidation(orderRequest.getOrderDetails().getShippingStatus()) &&
                BasicValidation.stringValidation(orderRequest.getOrderDetails().getPaymentType()) &&
                BasicValidation.stringValidation(orderRequest.getOrderDetails().getPaymentStatus()) &&
                Objects.nonNull(orderRequest.getOrderDetails().getOrderDate());// &&
//                orderRequest.getOrderDetails().getOrderDate().toLocalDate().isEqual(LocalDate.now());
    }

    public static boolean validateOrderItemRequestSchema(OrderItemDto orderItemDto) {
        return Objects.nonNull(orderItemDto.getProductId()) &&
                Objects.nonNull(orderItemDto.getQuantity()) &&
                Objects.nonNull(orderItemDto.getProductPrice());
    }

    public static boolean checkDuplicateOrderItem(List<OrderItemDto> orderItemDtoList) {
        List<Integer> productIds = orderItemDtoList.stream().map(OrderItemDto::getProductId).toList();
        return HelperFunctions.checkDuplicatesInList(productIds);
    }

    public static boolean validateOrderItemsList(List<OrderItemDto> orderItemDtoList) {
        return orderItemDtoList.stream().filter(OrderValidation::validateOrderItemRequestSchema).
                toList().size() == orderItemDtoList.size();
    }

    public static boolean validateOrderDtoSchema(OrderDto orderDto) {
        return Objects.nonNull(orderDto) &&
                Objects.nonNull(orderDto.getPaymentStatus()) &&
                Objects.nonNull(orderDto.getPaymentType()) &&
                Objects.nonNull(orderDto.getShippingStatus()) &&
                Objects.nonNull(orderDto.getDeliveryDate()) &&
                BasicValidation.stringValidation(orderDto.getPaymentStatus()) &&
                BasicValidation.stringValidation(orderDto.getPaymentType()) &&
                BasicValidation.stringValidation(orderDto.getShippingStatus());// &&
//                (orderDto.getDeliveryDate().toLocalDate().isAfter(LocalDate.now()) ||
//                        orderDto.getDeliveryDate().toLocalDate().isEqual(LocalDate.now()));
    }

    public static Object validateCustomerAndOrderItems(
            Integer customerId,
            List<OrderItemDto> orderItemDtoList,
            ProductService productService,
            CustomerService customerService
    ) {
        List<Integer> productIds = orderItemDtoList.stream().map(OrderItemDto::getProductId).toList();
        List<Integer> quantityList = orderItemDtoList.stream().map(OrderItemDto::getQuantity).toList();
        return HelperFunctions.validateCustomerAndProductQuantities(
                customerId,
                productIds,
                quantityList,
                productService,
                customerService
        );
    }
}
