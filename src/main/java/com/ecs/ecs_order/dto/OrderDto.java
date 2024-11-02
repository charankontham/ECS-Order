package com.ecs.ecs_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Integer orderId;
    private Integer customerId;
    private Integer addressId;
    private String paymentType;
    private String paymentStatus;
    private LocalDate orderDate;
    private LocalDate deliveryDate;
    private String shippingStatus;
}
