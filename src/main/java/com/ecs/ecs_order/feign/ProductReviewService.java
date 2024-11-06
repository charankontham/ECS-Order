package com.ecs.ecs_order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="ECS-REVIEWS", configuration = FeignClientConfig.class)
public interface ProductReviewService {
    @DeleteMapping("/api/productReview/deleteByCustomerId/{customerId}")
    ResponseEntity<String> deleteProductReviewByCustomerId(@PathVariable("customerId") Integer customerId);

    @DeleteMapping("/api/productReview/deleteByProductId/{productId}")
    ResponseEntity<String> deleteProductReviewByProductId(@PathVariable("productId") Integer productId);
}
