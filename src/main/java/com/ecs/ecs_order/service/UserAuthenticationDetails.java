package com.ecs.ecs_order.service;

import com.ecs.ecs_order.dto.AdminDto;
import com.ecs.ecs_order.dto.CustomerDto;
import com.ecs.ecs_order.dto.UserDto;
import com.ecs.ecs_order.dto.UserPrincipal;
import com.ecs.ecs_order.exception.ResourceNotFoundException;
import com.ecs.ecs_order.feign.AdminService;
import com.ecs.ecs_order.feign.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserAuthenticationDetails implements UserDetailsService {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private AdminService adminService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<CustomerDto> customerResponse = customerService.getCustomerByEmail(username);
        if (Objects.nonNull(customerResponse.getBody()) || customerResponse.getStatusCode() == HttpStatus.OK) {
            return new UserPrincipal(customerResponse.getBody());
        }
        ResponseEntity<AdminDto> adminResponse = adminService.getByUsername(username);
        if(adminResponse.getStatusCode() == HttpStatus.OK && Objects.nonNull(adminResponse.getBody())){
            return new UserPrincipal(adminResponse.getBody());
        }
        throw new ResourceNotFoundException("User not found");
    }
}
