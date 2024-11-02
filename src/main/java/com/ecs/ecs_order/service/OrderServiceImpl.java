package com.ecs.ecs_order.service;

import com.ecs.ecs_order.dto.OrderDto;
import com.ecs.ecs_order.dto.OrderFinalDto;
import com.ecs.ecs_order.dto.OrderItemDto;
import com.ecs.ecs_order.dto.OrderRequest;
import com.ecs.ecs_order.entity.Order;
import com.ecs.ecs_order.entity.OrderItem;
import com.ecs.ecs_order.exception.ResourceNotFoundException;
import com.ecs.ecs_order.feign.CustomerService;
import com.ecs.ecs_order.feign.ProductService;
import com.ecs.ecs_order.mapper.OrderItemMapper;
import com.ecs.ecs_order.mapper.OrderMapper;
import com.ecs.ecs_order.repository.CartItemRepository;
import com.ecs.ecs_order.repository.OrderItemRepository;
import com.ecs.ecs_order.repository.OrderRepository;
import com.ecs.ecs_order.service.interfaces.IOrderService;
import com.ecs.ecs_order.util.Constants;
import com.ecs.ecs_order.util.HelperFunctions;
import com.ecs.ecs_order.validations.AddressValidation;
import com.ecs.ecs_order.validations.OrderValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public OrderFinalDto getOrderById(int orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
        List<OrderItemDto> orderItemDtoList = orderItemRepository.findByOrderId(order.getOrderId()).
                stream().map(OrderItemMapper::mapToOrderItemDto).toList();
        return OrderMapper.toOrderFinalDto(order, orderItemDtoList, customerService, productService);
    }

    @Override
    public List<OrderFinalDto> getAllOrdersByCustomerId(int customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        List<List<OrderItemDto>> listOfOrderItemsList = HelperFunctions.
                getAllOrderItemsList(orders, orderItemRepository);
        AtomicInteger index = new AtomicInteger();
        return orders.stream().
                map((order) -> OrderMapper.toOrderFinalDto(
                        order,
                        listOfOrderItemsList.get(index.getAndIncrement()),
                        customerService, productService)
                ).collect(Collectors.toList());
    }

    @Override
    public List<OrderFinalDto> getAllOrdersByProductId(int productId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByProductId(productId);
        List<Order> orders = orderItems.stream().map(order -> orderRepository.findById(order.getOrderId()).orElse(null)).toList();
        List<List<OrderItemDto>> listOfOrderItemsList = HelperFunctions.
                getAllOrderItemsList(orders, orderItemRepository);
        AtomicInteger index = new AtomicInteger();
        return orders.stream().
                map((order) -> OrderMapper.toOrderFinalDto(
                        order,
                        listOfOrderItemsList.get(index.getAndIncrement()),
                        customerService,
                        productService)
                ).collect(Collectors.toList());
    }

    @Override
    public List<OrderFinalDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().
                map((order) -> {
                    List<OrderItemDto> orderItemsList = HelperFunctions.
                            getOrderItemsList(
                                    order.getOrderId(),
                                    orderItemRepository
                            );
                    return OrderMapper.toOrderFinalDto(order, orderItemsList, customerService, productService);
                }).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Object addOrder(OrderRequest orderRequest) {
        boolean orderIdExists = Objects.nonNull(orderRequest.getOrderDetails().getOrderId());
        if (orderIdExists) {
            if (orderRepository.existsById(orderRequest.getOrderDetails().getOrderId())) {
                return HttpStatus.CONFLICT;
            }
        }
        return validateAndSaveOrder(orderRequest);
    }

    @Override
    public Object updateOrder(OrderDto orderDto) {
        if(Objects.isNull(orderDto.getOrderId())) {
            throw new ResourceNotFoundException("OrderId not found!");
        }
        Order order = orderRepository.findById(orderDto.getOrderId()).
                orElseThrow(() -> new ResourceNotFoundException("Order Not Found!"));
        if (order.getClass().equals(Order.class)) {
            OrderDto existingOrderDto = OrderMapper.toOrderDto(order);
            orderDto.setCustomerId(existingOrderDto.getCustomerId());
            orderDto.setAddressId(existingOrderDto.getAddressId());
            orderDto.setOrderDate(existingOrderDto.getOrderDate());
            return validateAndUpdateOrder(orderDto);
        }
        return Constants.OrderNotFound;
    }

    private Object validateAndUpdateOrder(OrderDto orderDto) {
        if (!OrderValidation.validateOrderDtoSchema(orderDto)) {
            return HttpStatus.BAD_REQUEST;
        }
        Order updatedOrder = orderRepository.save(OrderMapper.toOrder(orderDto));
        List<OrderItemDto> orderItemsList = HelperFunctions.getOrderItemsList(
                orderDto.getOrderId(),
                orderItemRepository
        );
        return OrderMapper.toOrderFinalDto(updatedOrder, orderItemsList, customerService, productService);
    }

    private Object validateAndSaveOrder(OrderRequest orderRequest) {
        if (!OrderValidation.validateOrderRequestSchema(orderRequest)) {
            return HttpStatus.BAD_REQUEST;
        }
        Object response = OrderValidation.validateCustomerAndOrderItems(
                orderRequest.getOrderDetails().getCustomerId(),
                orderRequest.getOrderItems(),
                productService,
                customerService
        );
        if (Objects.equals(response, Constants.NoErrorFound)) {
            response = AddressValidation.validateAddress(
                    orderRequest.getOrderDetails().getAddressId(),
                    orderRequest.getOrderDetails().getCustomerId(),
                    customerService);
            if (Objects.equals(response, Constants.NoErrorFound)) {
                Order savedOrder = orderRepository.save(OrderMapper.toOrder(orderRequest.getOrderDetails()));
                orderRequest.getOrderItems()
                        .forEach(orderItem -> orderItem.setOrderId(savedOrder.getOrderId()));
                orderItemRepository.saveAll(
                        orderRequest.getOrderItems().
                                stream().
                                map(OrderItemMapper::mapToOrderItem).
                                toList()
                );
                orderRequest.getOrderItems().forEach(orderItem -> cartItemRepository.
                        deleteByCustomerIdAndProductId(
                                orderRequest.getOrderDetails().getCustomerId(),
                                orderItem.getProductId()
                        )
                );
                return OrderMapper.toOrderFinalDto(
                        savedOrder,
                        orderRequest.getOrderItems(),
                        customerService,
                        productService
                );
            }
        }
        return response;
    }

    @Transactional
    @Override
    public void deleteOrderById(int orderId) {
        orderItemRepository.deleteByOrderId(orderId);
        orderRepository.deleteById(orderId);
    }
}
