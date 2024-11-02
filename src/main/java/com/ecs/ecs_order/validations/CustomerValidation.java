package com.ecs.ecs_order.validations;

import com.ecs.ecs_order.dto.CustomerDto;

public class CustomerValidation {
    public static boolean validateCustomer(CustomerDto customerDto) {
        return BasicValidation.stringValidation(customerDto.getCustomerName()) ||
                BasicValidation.stringValidation(customerDto.getEmail()) ||
                BasicValidation.stringValidation(customerDto.getPassword());
    }
}
