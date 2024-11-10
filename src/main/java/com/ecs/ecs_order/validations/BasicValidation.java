package com.ecs.ecs_order.validations;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicValidation {
    public static boolean stringValidation(String value) {
        return !Objects.isNull(value) && !value.trim().isEmpty() && !value.equals("null");
    }

    public static boolean passwordValidation(String value) {
        if (Objects.isNull(value) || value.trim().isEmpty() || value.equals("null")) {
            return false;
        }
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8, 20}$";
        Pattern regexPattern = Pattern.compile(regex);
        Matcher matcher = regexPattern.matcher(value);
        return matcher.matches();
    }
}
