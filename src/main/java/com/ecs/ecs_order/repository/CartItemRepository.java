package com.ecs.ecs_order.repository;

import com.ecs.ecs_order.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findAllByCustomerId(Integer customerId);

    void deleteByCustomerIdAndProductId(Integer customerId, Integer productId);

    boolean existsByProductId(Integer productId);
}
