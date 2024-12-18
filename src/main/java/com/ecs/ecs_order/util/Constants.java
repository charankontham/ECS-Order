package com.ecs.ecs_order.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    public final static int OrderInvoiceNotGenerated = 13;
}
