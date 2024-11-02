package com.ecs.ecs_order.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@AllArgsConstructor
@Getter
public class Constants {
    public static final int ProductNotFound = 1;
    public final static int CustomerNotFound = 2;
    public final static int ProductCategoryNotFound = 3;
    public final static int CartItemNotFound = 4;
    public final static int OrderNotFound = 5;
    public final static int UserNotFound = 6;
    public final static int AddressNotFound = 7;
    public final static int ProductQuantityExceeded = 8;
    public final static int CartEmpty = 9;
    public final static int NoErrorFound = 10;
    public final static int ProductReviewNotFound = 11;
    public final static int ProductBrandNotFound = 12;

    private final static String ErrorCodesString = """
            {
                "ProductNotFound":1,
                "CustomerNotFound":2,
                "ProductCategoryNotFound":3,
                "CartNotFound":4,
                "OrderNotFound":5,
                "UserNotFound":6,
                "AddressNotFound":7,
                "ProductQuantityExceeded":8,
                "CartEmpty":9,
                "NoErrorFound":10
            }
            """;

    private final static String colorString = """
            {
              "White" : 1,
              "Black" : 2,
              "Pink" : 3,
              "Blue" : 4,
              "Orange" : 5,
              "Green" : 6,
              "Brown" : 7,
              "Grey" : 8,
              "Red" : 10,
              "Yellow" : 11
            }""";

    public final static JSONObject ProductColors;
    public final static JSONObject ErrorCodes;

    static {
        try {
            ProductColors = (JSONObject) new JSONParser().parse(colorString);
            ErrorCodes = (JSONObject) new JSONParser().parse(ErrorCodesString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
