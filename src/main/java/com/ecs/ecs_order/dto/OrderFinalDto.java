package com.ecs.ecs_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderFinalDto {
    private int orderId;
    private CustomerDto customer;
    private AddressDto shippingAddress;
    private List<ProductFinalDto> orderItems;
    private String paymentType;
    private String paymentStatus;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String shippingStatus;
}
