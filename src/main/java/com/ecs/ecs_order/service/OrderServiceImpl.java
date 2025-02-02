package com.ecs.ecs_order.service;

import com.ecs.ecs_order.dto.*;
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

import java.io.File;
import java.util.*;
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
    @Autowired
    private InvoiceGeneratorService invoiceGeneratorService;
    @Autowired
    private S3Service s3Service;

    @Override
    public OrderFinalDto getOrderById(Integer orderId) {
        Order order = orderRepository.findById(orderId).
                orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
        List<OrderItemDto> orderItemDtoList = orderItemRepository.findByOrderId(order.getOrderId()).
                stream().map(OrderItemMapper::mapToOrderItemDto).toList();
        return OrderMapper.toOrderFinalDto(order, orderItemDtoList, customerService, productService);
    }

    @Override
    public List<OrderFinalDto> getAllOrdersByCustomerId(Integer customerId) {
        List<Order> orders = orderRepository.findAllOrdersByCustomerIdOrderByOrderIdDesc(customerId);
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
    public List<OrderFinalDto> getAllOrdersByProductId(Integer productId) {
        List<OrderItem> orderItems = orderItemRepository.findAllByProductId(productId);
        List<Order> orders = orderItems.stream().
                map(order -> orderRepository.findById(order.getOrderId()).orElse(null)).toList();
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
    public Boolean existsByProductId(Integer productId) {
        return !getAllOrdersByProductId(productId).isEmpty();
    }

    @Override
    public List<OrderFinalDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().
                map((order) -> {
                    List<OrderItemDto> orderItemsList = HelperFunctions.
                            getOrderItemsList(order.getOrderId(), orderItemRepository);
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
    public Object updateOrder(OrderDto orderDto) throws Exception {
        System.out.println("Entered into updateOrder method");
        if (Objects.isNull(orderDto.getOrderId())) {
            throw new ResourceNotFoundException("OrderId not found!");
        }
        Order order = orderRepository.findById(orderDto.getOrderId()).
                orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
        if (order.getClass().equals(Order.class)) {
            OrderDto existingOrderDto = OrderMapper.toOrderDto(order);
            orderDto.setCustomerId(existingOrderDto.getCustomerId());
            orderDto.setAddressId(existingOrderDto.getAddressId());
            orderDto.setOrderDate(existingOrderDto.getOrderDate());
            System.out.println("Order found and continuing");
            return validateAndUpdateOrder(orderDto);
        }
        return Constants.OrderNotFound;
    }

    private Object validateAndUpdateOrder(OrderDto orderDto) throws Exception {
        if (!OrderValidation.validateOrderDtoSchema(orderDto)) {
            return HttpStatus.BAD_REQUEST;
        }
        if (orderDto.getShippingStatus().equals("Shipped")) {
            System.out.println("=== entered into invoice generation block ===");
            InvoiceData invoiceData = new InvoiceData();
            List<OrderItemDto> orderItems = orderItemRepository
                    .findByOrderId(orderDto.getOrderId()).stream().map(OrderItemMapper::mapToOrderItemDto).toList();
            AddressDto address = customerService.getAddressById(
                    orderDto.getAddressId()
            ).getBody();
            invoiceData.setProducts(
                    HelperFunctions.convertFromOrderItemsToProductDtoList(
                            orderItems,
                            productService
                    ));
            invoiceData.setBillingAddress(
                    address
            );
            invoiceData.setShippingAddress(address);
            invoiceData.setOrderDate(orderDto.getOrderDate());
            invoiceData.setOrderDate(orderDto.getDeliveryDate());
            invoiceData.setTotalTax(HelperFunctions.calculateTotalTax(orderItems));
            invoiceData.setTotalOrderValue(HelperFunctions.calculateTotalPrice(orderItems, orderDto.getShippingFee()));
            invoiceData.setOrderId(orderDto.getOrderId());
            System.out.println("=== Ready to generate invoice ===");
            invoiceGeneratorService.generateInvoice("invoice.pdf", invoiceData);
            System.out.println("=== Invoice Generated! ===");
            File file = new File("invoice.pdf");
            String key = "invoices/invoice_" + orderDto.getOrderId() + ".pdf";
            String s3Url = s3Service.uploadFile(file, key);
            file.delete();
            System.out.println(s3Url);
        }

        Order existingOrder = orderRepository.findById(orderDto.getOrderId()).orElseThrow(() -> new ResourceNotFoundException("Order not found!"));
        existingOrder.setShippingStatus(orderDto.getShippingStatus());
        existingOrder.setAddressId(orderDto.getAddressId());
        existingOrder.setDeliveryDate(orderDto.getDeliveryDate());
        existingOrder.setPaymentStatus(orderDto.getPaymentStatus());
        existingOrder.setPaymentType(orderDto.getPaymentType());
        Order updatedOrder = orderRepository.save(existingOrder);
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
                Order savedOrder = orderRepository.save(OrderMapper.toOrder(orderRequest.getOrderDetails(), orderRequest.getOrderItems()));
                orderRequest.getOrderItems()
                        .forEach(orderItem -> orderItem.setOrderId(savedOrder.getOrderId()));
                orderItemRepository.saveAll(orderRequest.getOrderItems().stream().
                        map(OrderItemMapper::mapToOrderItem).toList());
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
    public void deleteOrderById(Integer orderId) {
        orderItemRepository.deleteByOrderId(orderId);
        orderRepository.deleteById(orderId);
    }

    @Override
    public List<OrderItemDto> getOrderItemsByProductId(Integer productId) {
        return orderItemRepository.findAllByProductId(productId).stream().
                map(OrderItemMapper::mapToOrderItemDto).toList();
    }

    @Override
    public List<ProductFinalDto> getOrderItemsByCustomerId(Integer customerId) {
        List<ProductFinalDto> allUniqueOrderItems = new ArrayList<>();
        Set<Integer> uniqueProducts = new LinkedHashSet<>();
        getAllOrdersByCustomerId(customerId).forEach((order) -> {
            order.getOrderItems().forEach(orderItem -> {
                uniqueProducts.add(orderItem.getProductId());
            });
        });
        System.out.println("uniqueProducts: " + uniqueProducts.stream().toList());
        for(Integer productId : uniqueProducts) {
            allUniqueOrderItems.add(productService.getProductById(productId).getBody());
        }
        return allUniqueOrderItems;
    }

    @Override
    public Object downloadOrderInvoice(Integer invoiceId) {
        boolean orderExists = orderRepository.existsById(invoiceId);
        if (orderExists) {
            Order order = orderRepository.findById(invoiceId).orElse(null);
            if(order != null && order.getShippingStatus().equals("OrderPlaced")) {
                return Constants.OrderInvoiceNotGenerated;
            }
            String key = "invoices/invoice_" + invoiceId + ".pdf";
            Object fileResponse = s3Service.downloadFile(key, "invoice_" + invoiceId + ".pdf");
            if(fileResponse.equals(HttpStatus.NOT_FOUND)) {
                return HttpStatus.NOT_FOUND;
            }else{
                return fileResponse;
            }
        } else {
            return Constants.OrderNotFound;
        }
    }
}
