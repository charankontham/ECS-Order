package com.ecs.ecs_order.repository;

import com.ecs.ecs_order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllOrdersByCustomerIdOrderByOrderIdDesc(Integer customerId);
}
