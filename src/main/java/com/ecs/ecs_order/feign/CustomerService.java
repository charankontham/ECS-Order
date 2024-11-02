package com.ecs.ecs_order.feign;

import com.ecs.ecs_order.dto.AddressDto;
import com.ecs.ecs_order.dto.CustomerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient("ECS-CUSTOMER")
public interface CustomerService {

    @PostMapping("/api/customer/")
    ResponseEntity<?> addCustomer(@RequestBody CustomerDto customerDto);

    @GetMapping("/api/customer/")
    ResponseEntity<List<CustomerDto>> getAllCustomers();

    @GetMapping("/api/customer/{id}")
    ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") int customerId);

    @RequestMapping(value = "/api/customer/getByEmail", method = RequestMethod.GET, params="email")
    ResponseEntity<CustomerDto> getCustomerByEmail(@RequestParam("email") String email);

    @PutMapping("/api/customer")
    ResponseEntity<?> updateCustomer(@RequestBody CustomerDto customerDto);

    @DeleteMapping("/api/customer/{id}")
    ResponseEntity<String> deleteCustomer(@PathVariable("id") int customerId);

    @GetMapping("/api/customer/login")
    ResponseEntity<Object> customerLogin(@RequestParam String email, @RequestParam String password);

    @GetMapping("/api/address/{id}")
    ResponseEntity<AddressDto> getAddressById(@PathVariable("id") int addressId);

    @DeleteMapping("/api/address/deleteAddressByCustomerId/{id}")
    ResponseEntity<String> deleteAddressByCustomerId(@PathVariable("id") int customerId);
}
