package com.ecs.ecs_order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubCategoryEnriched {
    private Integer subCategoryId;
    private ProductCategoryDto ProductCategory;
    private String subCategoryName;
    private String subCategoryDescription;
    private String subCategoryImage;
}
