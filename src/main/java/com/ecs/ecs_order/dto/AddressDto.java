package com.ecs.ecs_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private int addressId;
    private int customerId;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
}
