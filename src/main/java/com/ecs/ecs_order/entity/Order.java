package com.ecs.ecs_order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Columns;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "address_id")
    private Integer addressId;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "items_subtotal")
    private Float itemsSubtotal;

    @Column(name = "shipping_fee")
    private Float shippingFee;

    @Column(name = "total_tax")
    private Float totalTax;

    @Column(name = "total_order_amount")
    private Float totalOrderAmount;

    @Column(name = "ordered_date")
    private LocalDateTime orderDate;

    @Column(name = "delivery_date")
    private LocalDateTime deliveryDate;

    @Column(name = "delivery_status")
    private String shippingStatus;
}
