package com.ecs.ecs_order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class InvoiceData {
    private Integer orderId;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private AddressDto billingAddress;
    private AddressDto shippingAddress;
    private List<ProductFinalDto> products;
    private double totalTax;
    private double totalOrderValue;
}
