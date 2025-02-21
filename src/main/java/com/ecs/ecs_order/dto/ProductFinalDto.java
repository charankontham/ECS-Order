package com.ecs.ecs_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFinalDto {
    private Integer productId;
    private String productName;
    private ProductBrandDto brand;
    private SubCategoryEnriched productSubCategory;
    private String productDescription;
    private Float productPrice;
    private Integer productQuantity;
    private String productImage;
    private String productColor;
    private Float productWeight;
    private String productDimensions;
    private String productCondition;
}
