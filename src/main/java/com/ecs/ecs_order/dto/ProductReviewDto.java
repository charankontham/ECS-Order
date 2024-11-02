package com.ecs.ecs_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductReviewDto {
    private int reviewId;
    private int productId;
    private int customerId;
    private String productReview;
    private float productRating;
}
