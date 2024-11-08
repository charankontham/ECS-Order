package com.ecs.ecs_order.util;

import com.ecs.ecs_order.dto.*;
import com.ecs.ecs_order.entity.Order;
import com.ecs.ecs_order.exception.ResourceNotFoundException;
import com.ecs.ecs_order.feign.CustomerService;
import com.ecs.ecs_order.feign.ProductService;
import com.ecs.ecs_order.mapper.OrderItemMapper;
import com.ecs.ecs_order.repository.OrderItemRepository;
import lombok.Setter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

@Setter
public class HelperFunctions {

    public static boolean checkZeroQuantities(List<Integer> quantities) {
        for (Integer quantity : quantities) {
            if (quantity == 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkDuplicatesInList(List<Integer> list) {
        Set<Integer> set = new HashSet<>(list);
        return set.size() == list.size();
    }

    public static List<ProductFinalDto> getProductFinalDtoList( List<Integer> productIdsList, ProductService productService) {
        return productIdsList.
                stream().map(productService::getProductById).toList().
                stream().filter((response) -> {
                    if(Objects.equals(response.getStatusCode(), HttpStatus.OK)){
                        return true;
                    } else {
                        throw new ResourceNotFoundException("Product not found");
                    }
                }).toList().
                stream().map(HttpEntity::getBody).toList();
    }

    public static ResponseEntity<?> getResponseEntity(Object response) {
        if (Objects.equals(response, Constants.ProductNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product Not Found!");
        } else if (Objects.equals(response, Constants.CustomerNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Not Found!");
        } else if (Objects.equals(response, Constants.ProductQuantityExceeded)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ProductQuantities Exceeded!");
        } else if (Objects.equals(response, Constants.AddressNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Address Not Found!");
        } else if (Objects.equals(response, Constants.OrderNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Not Found!");
        } else if (Objects.equals(response, Constants.CartItemNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CartItem Not Found!");
        } else if (Objects.equals(response, Constants.UserNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!");
        } else if (Objects.equals(response, Constants.ProductCategoryNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ProductCategory Not Found!");
        } else if (Objects.equals(response, Constants.ProductBrandNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ProductBrand Not Found!");
        } else if (Objects.equals(response, Constants.ProductReviewNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ProductReview Not Found!");
        } else if (Objects.equals(response, HttpStatus.CONFLICT)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Duplicate Entry!");
        } else if (Objects.equals(response, HttpStatus.BAD_REQUEST)) {
            return new ResponseEntity<>("Validation Failed/Bad Request!", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public static List<List<OrderItemDto>> getAllOrderItemsList(List<Order> orderList, OrderItemRepository orderItemRepository) {
        List<List<OrderItemDto>> allOrderItemsList = new ArrayList<>();
        for(Order order : orderList) {
            allOrderItemsList.add(getOrderItemsList(order.getOrderId(), orderItemRepository));
        }
        return allOrderItemsList;
    }

    public static List<OrderItemDto> getOrderItemsList(Integer orderId, OrderItemRepository orderItemRepository) {
        return orderItemRepository.findByOrderId(orderId).stream().map(OrderItemMapper::mapToOrderItemDto).toList();
    }

    public static Object validateCustomerAndProductQuantities(Integer customerId,
                                                              List<Integer> productIds,
                                                              List<Integer> quantityList,
                                                              ProductService productService,
                                                              CustomerService customerService)
    {
        try {
            boolean customerExists = customerService.getCustomerById(customerId).getStatusCode()== HttpStatus.OK;
            if (!customerExists) {
                return Constants.CustomerNotFound;
            }
            List<ProductFinalDto> productFinalDtoList = getProductFinalDtoList(
                    productIds,
                    productService
            );
            int count = 0;
            for (ProductFinalDto inventoryProduct : productFinalDtoList) {
                if (quantityList.get(count++) > inventoryProduct.getProductQuantity()) {
                    throw new RuntimeException("Product Quantity Exceeded!");
                }
            }
            return Constants.NoErrorFound;
        } catch (ResourceNotFoundException e) {
            if (e.getMessage().contains("Product")) {
                return Constants.ProductNotFound;
            }
            System.out.println("Error: " + e.getMessage());
            return HttpStatus.BAD_REQUEST;
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Quantity Exceeded!")) {
                return Constants.ProductQuantityExceeded;
            } else {
                System.out.println("Error: " + e.getMessage());
                return HttpStatus.BAD_REQUEST;
            }
        }
    }

    public static boolean isFutureOrToday(Date date) {
        Calendar todayDate = removeTimeFromDate(new Date());
        Calendar givenDate = removeTimeFromDate(date);

        return !givenDate.before(todayDate);
    }

    public static boolean isToday(Date date){
        Calendar todayDate = removeTimeFromDate(new Date());
        Calendar givenDate = removeTimeFromDate(date);
        return givenDate.equals(todayDate);
    }

    public static Calendar removeTimeFromDate(Date givenDate) {
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(givenDate);
        calendarDate.set(Calendar.HOUR_OF_DAY, 0);
        calendarDate.set(Calendar.MINUTE, 0);
        calendarDate.set(Calendar.SECOND, 0);
        calendarDate.set(Calendar.MILLISECOND, 0);
        return calendarDate;
    }

}
