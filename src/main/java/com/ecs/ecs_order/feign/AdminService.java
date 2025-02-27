package com.ecs.ecs_order.feign;

import com.ecs.ecs_order.config.FeignClientConfig;
import com.ecs.ecs_order.dto.AdminDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ECS-INVENTORY-ADMIN", configuration = FeignClientConfig.class)
public interface AdminService {
    @GetMapping("/api/admin/getByUsername/{username}")
    ResponseEntity<AdminDto> getByUsername(@PathVariable("username") String username);
}
