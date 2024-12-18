package com.ecs.ecs_order.feign;

import com.ecs.ecs_order.config.FeignClientConfig;
import com.ecs.ecs_order.dto.ProductFinalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ECS-PRODUCT", configuration = FeignClientConfig.class)
public interface ProductService {
    @GetMapping("/api/product/{id}")
    ResponseEntity<ProductFinalDto> getProductById(@PathVariable("id") Integer productId);
}
