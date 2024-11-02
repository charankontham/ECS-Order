package com.ecs.ecs_order.validations;

import com.ecs.ecs_order.dto.UserDto;

import java.util.Objects;

public class UserValidation {

    public static boolean validateUser(UserDto userDto){
        return BasicValidation.stringValidation(userDto.getUsername())
                && BasicValidation.stringValidation(userDto.getPassword())
                && Objects.nonNull(userDto.getRole()) ;
    }
}
