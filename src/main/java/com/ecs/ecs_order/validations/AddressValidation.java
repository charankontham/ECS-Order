package com.ecs.ecs_order.validations;

import com.ecs.ecs_order.dto.AddressDto;
import com.ecs.ecs_order.exception.ResourceNotFoundException;
import com.ecs.ecs_order.feign.CustomerService;
import com.ecs.ecs_order.util.Constants;

import java.util.Objects;

public class AddressValidation {
    public static Object validateAddress(Integer addressId, Integer customerId, CustomerService customerService) {
        try {
            AddressDto addressDto = customerService.getAddressById(addressId).getBody();
            if (!Objects.equals(Objects.requireNonNull(addressDto).getCustomerId(), customerId)) {
                return Constants.AddressNotFound;
            }
            return Constants.NoErrorFound;
        } catch (ResourceNotFoundException e) {
            if (e.getMessage().contains("Address")) {
                return Constants.AddressNotFound;
            }
            return e.getMessage();
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }
}
