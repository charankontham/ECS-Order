package com.ecs.ecs_order.feign;

import com.ecs.ecs_order.config.FeignClientConfig;
import com.ecs.ecs_order.dto.AddressDto;
import com.ecs.ecs_order.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ECS-CUSTOMER", configuration = FeignClientConfig.class)
public interface CustomerService {
    @GetMapping("/api/customer/{id}")
    ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") Integer customerId);

    @GetMapping("/api/address/{id}")
    ResponseEntity<AddressDto> getAddressById(@PathVariable("id") Integer addressId);

    @GetMapping("/api/customer/getByEmail/{email}")
    ResponseEntity<CustomerDto> getCustomerByEmail(@PathVariable("email") String email);
}
