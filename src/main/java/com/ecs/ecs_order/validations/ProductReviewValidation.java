package com.ecs.ecs_order.validations;

import com.ecs.ecs_order.dto.ProductReviewDto;

import java.util.Objects;

public class ProductReviewValidation {
    public static boolean validateProductReview(ProductReviewDto productReviewDto) {
        try {
            return Objects.nonNull(productReviewDto) &&
                    productReviewDto.getProductId() != 0 &&
                    productReviewDto.getCustomerId() != 0;
        }catch(Exception exception){
            return false;
        }

    }
}
