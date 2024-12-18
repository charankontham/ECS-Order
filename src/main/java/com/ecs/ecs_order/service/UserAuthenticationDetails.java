package com.ecs.ecs_order.service;

import com.ecs.ecs_order.dto.CustomerDto;
import com.ecs.ecs_order.dto.UserDto;
import com.ecs.ecs_order.dto.UserPrincipal;
import com.ecs.ecs_order.exception.ResourceNotFoundException;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ResponseEntity<CustomerDto> response = customerService.getCustomerByEmail(username);
        if (Objects.isNull(response.getBody()) || response.getStatusCode() != HttpStatus.OK) {
            throw new ResourceNotFoundException("Customer not found!");
        } else {
            return new UserPrincipal(response.getBody());
        }
    }
}
