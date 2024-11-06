package com.ecs.ecs_order.feign;

import com.ecs.ecs_order.dto.ProductBrandDto;
import com.ecs.ecs_order.dto.ProductCategoryDto;
import com.ecs.ecs_order.dto.ProductDto;
import com.ecs.ecs_order.dto.ProductFinalDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@FeignClient(name="ECS-PRODUCT", configuration = FeignClientConfig.class)
public interface ProductService {
    @GetMapping("/api/product/{id}")
    ResponseEntity<ProductFinalDto> getProductById(@PathVariable("id") Integer productId);

    @GetMapping("/api/product/")
    ResponseEntity<List<ProductFinalDto>> getAllProducts();

    @PostMapping("/api/product")
    ResponseEntity<?> addProduct(@RequestBody ProductDto productDto);

    @PutMapping("/api/product")
    ResponseEntity<?> updateProducts(@RequestBody List<ProductFinalDto> productFinalDtoList);

    @DeleteMapping("/api/product/{id}")
    ResponseEntity<?> deleteProduct(@PathVariable("id") int productId);

    @GetMapping("/api/productCategory/{id}")
    ResponseEntity<ProductCategoryDto> getProductCategoryById(@PathVariable("id") int categoryId);

    @GetMapping("/api/productCategory/")
    ResponseEntity<?> getAllProductCategory();

    @PostMapping("/api/productCategory")
    ResponseEntity<?> addProductCategory(@RequestBody ProductCategoryDto productCategoryDto);

    @PutMapping("/api/productCategory")
    ResponseEntity<?> updateProductCategory(@RequestBody ProductCategoryDto productCategoryDto);

    @DeleteMapping("/api/productCategory/{id}")
    ResponseEntity<String> deleteProductCategory(@PathVariable("id") int productCategoryId);

    @GetMapping("/api/productBrand/{id}")
    ResponseEntity<ProductBrandDto> getProductBrandById(@PathVariable("id") int brandId);

    @GetMapping("/api/productBrand/")
    ResponseEntity<List<ProductBrandDto>> getAllProductBrands();

    @PostMapping("/api/productBrand")
    ResponseEntity<?> addProductBrand(@RequestBody ProductBrandDto productBrandDto);

    @PutMapping("/api/productBrand")
    ResponseEntity<?> updateProductBrand(@RequestBody ProductBrandDto productBrandDto);

    @DeleteMapping("/api/productBrand/{id}")
    ResponseEntity<?> deleteProductBrand(@PathVariable("id") int brandId);
}
