package com.ecs.ecs_order.repository;

import com.ecs.ecs_order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderId(Integer orderId);
    void deleteByOrderId(Integer orderId);
    List<OrderItem> findAllByProductId(Integer productId);
}
