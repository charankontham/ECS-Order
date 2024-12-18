package com.ecs.ecs_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFinalDto {
    private Integer orderId;
    private CustomerDto customer;
    private AddressDto shippingAddress;
    private List<ProductFinalDto> orderItems;
    private Float itemsSubTotal;
    private Float totalTax;
    private Float totalOrderValue;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private String shippingStatus;
    private String paymentType;
    private String paymentStatus;
}
