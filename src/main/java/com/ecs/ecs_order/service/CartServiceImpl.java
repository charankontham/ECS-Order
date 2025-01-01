package com.ecs.ecs_order.service;

import com.ecs.ecs_order.dto.CartDto;
import com.ecs.ecs_order.dto.CartFinalDto;
import com.ecs.ecs_order.dto.CartItemDto;
import com.ecs.ecs_order.entity.CartItem;
import com.ecs.ecs_order.feign.CustomerService;
import com.ecs.ecs_order.feign.ProductService;
import com.ecs.ecs_order.mapper.CartItemMapper;
import com.ecs.ecs_order.mapper.CartMapper;
import com.ecs.ecs_order.repository.CartItemRepository;
import com.ecs.ecs_order.service.interfaces.ICartService;
import com.ecs.ecs_order.util.Constants;
import com.ecs.ecs_order.validations.CartValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Override
    public CartFinalDto getCartByCustomerId(Integer customerId) {
        List<CartItem> cartItems = cartItemRepository.findAllByCustomerId(customerId);
        List<CartItemDto> cartItemDtoList = cartItems.stream().map(CartItemMapper::mapToCartItemDto).toList();
        return CartMapper.mapToCartFinalDto(customerId, cartItemDtoList, customerService, productService);
    }

    @Override
    @Transactional
    public Object addOrUpdateCartItem(CartDto cartDto) {
        return validateAndSaveCart(cartDto);
    }

    @Override
    public boolean deleteCartItem(Integer cartItemId) {
        boolean cartItemExists = cartItemRepository.existsById(cartItemId);
        if (cartItemExists) {
            cartItemRepository.deleteById(cartItemId);
            return true;
        }
        return false;
    }

    @Override
    public boolean isCartItemsExistsByProductId(Integer productId) {
        return cartItemRepository.existsByProductId(productId);
    }

    private Object validateAndSaveCart(CartDto cartDto) {
        if (!CartValidation.validateCartRequestSchema(cartDto)) {
            return HttpStatus.BAD_REQUEST;
        }
        Object response = CartValidation.validateCustomerAndCartItems(
                cartDto,
                productService,
                customerService
        );
        if (Objects.equals(response, Constants.NoErrorFound)) {
            List<CartItem> savedCartItems = cartItemRepository.saveAll(cartDto.getCartItems().stream().map(CartItemMapper::mapToCartItem).toList());
            return CartMapper.mapToCartFinalDto(
                    cartDto.getCustomerId(),
                    savedCartItems.stream().map(CartItemMapper::mapToCartItemDto).toList(),
                    customerService,
                    productService
            );
        }
        return response;
    }
}
