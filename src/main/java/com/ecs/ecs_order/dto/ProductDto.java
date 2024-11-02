package com.ecs.ecs_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private Integer productId;
    private Integer productCategoryId;
    private Integer productBrandId;
    private String productName;
    private String productDescription;
    private Float productPrice;
    private Integer productQuantity;
    private String productColor;
    private Float productWeight;
    private String productDimensions;
    private String productCondition;
}
