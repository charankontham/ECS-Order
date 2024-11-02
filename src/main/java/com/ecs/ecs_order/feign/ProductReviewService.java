package com.ecs.ecs_order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("ECS-REVIEWS")
public interface ProductReviewService {
    @DeleteMapping("/api/productReview/deleteByCustomerId/{customerId}")
    ResponseEntity<String> deleteProductReviewByCustomerId(@PathVariable("customerId") int customerId);

    @DeleteMapping("/api/productReview/deleteByProductId/{productId}")
    ResponseEntity<String> deleteProductReviewByProductId(@PathVariable("productId") int productId);
}
