package com.ecs.ecs_order.controller;

import com.ecs.ecs_order.dto.OrderDto;
import com.ecs.ecs_order.dto.OrderFinalDto;
import com.ecs.ecs_order.dto.OrderItemDto;
import com.ecs.ecs_order.dto.OrderRequest;
import com.ecs.ecs_order.service.interfaces.IOrderService;
import com.ecs.ecs_order.util.Constants;
import com.ecs.ecs_order.util.HelperFunctions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderFinalDto> getOrderById(@PathVariable("id") Integer orderId) {
        OrderFinalDto orderFinalDto = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderFinalDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderFinalDto>> getAllOrders() {
        List<OrderFinalDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrdersByCustomerId/{id}")
    public ResponseEntity<List<OrderFinalDto>> getAllOrdersByCustomerId(@PathVariable("id") Integer customerId) {
        List<OrderFinalDto> orders = orderService.getAllOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrdersByProductId/{id}")
    public ResponseEntity<List<OrderFinalDto>> getAllOrdersByProductId(@PathVariable("id") Integer productId) {
        List<OrderFinalDto> orders = orderService.getAllOrdersByProductId(productId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrderItemsByProductId/{id}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByProductId(@PathVariable("id") Integer productId) {
        List<OrderItemDto> orderItems = orderService.getOrderItemsByProductId(productId);
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping("/existsByProductId/{id}")
    public ResponseEntity<Boolean> existsByProductId(@PathVariable("id") Integer productId) {
        return new ResponseEntity<>(orderService.existsByProductId(productId), HttpStatus.OK);
    }

    @GetMapping("/downloadOrderInvoice/{id}")
    public ResponseEntity<?> downloadOrderInvoice(@PathVariable("id") Integer invoiceId) throws IOException {
        Object responseFile = orderService.downloadOrderInvoice(invoiceId);
        if (responseFile.equals(Constants.OrderNotFound)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE,"application/json")
                    .body("InvoiceId not found!");
        }else if(responseFile.equals(Constants.OrderInvoiceNotGenerated)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE,"application/json")
                    .body("Invoice not generated!");
        } else if (responseFile instanceof File) {
            try {
                Resource resource = new InputStreamResource(Files.newInputStream(((File) responseFile).toPath()));
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, "application/pdf",
                                HttpHeaders.CONTENT_DISPOSITION, "inline; filename=invoice_6.pdf")
                        .body(resource);
            } finally {
                boolean deleted = ((File) responseFile).delete();
                if (!deleted) {
                    System.err.println("Failed to delete temporary file: " + ((File) responseFile).getAbsolutePath());
                }
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .header(HttpHeaders.CONTENT_TYPE,"application/json")
                    .body("Invoice not found in S3 bucket!");
        }
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestBody OrderRequest orderRequest) {
        Object response = orderService.addOrder(orderRequest);
        return HelperFunctions.getResponseEntity(response);
    }

    @PutMapping
    public ResponseEntity<?> updateOrder(@RequestBody OrderDto orderDto) throws Exception {
        Object response = orderService.updateOrder(orderDto);
        return HelperFunctions.getResponseEntity(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Integer orderId) {
        orderService.deleteOrderById(orderId);
        return new ResponseEntity<>("Order deleted successfully", HttpStatus.OK);
    }

}
