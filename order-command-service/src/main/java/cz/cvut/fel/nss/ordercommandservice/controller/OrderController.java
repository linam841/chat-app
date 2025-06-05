package cz.cvut.fel.nss.ordercommandservice.controller;

import cz.cvut.fel.nss.ordercommandservice.dto.CreateOrderRequest;
import cz.cvut.fel.nss.ordercommandservice.model.Order;
import cz.cvut.fel.nss.ordercommandservice.pipeline.OrderProcessingException;
import cz.cvut.fel.nss.ordercommandservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            Order order = orderService.createOrder(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (OrderProcessingException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(OrderProcessingException.class)
    public ResponseEntity<String> handleOrderProcessingException(OrderProcessingException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
} 