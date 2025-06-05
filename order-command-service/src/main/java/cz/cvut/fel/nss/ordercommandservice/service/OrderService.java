package cz.cvut.fel.nss.ordercommandservice.service;

import cz.cvut.fel.nss.ordercommandservice.dto.CreateOrderRequest;
import cz.cvut.fel.nss.ordercommandservice.model.Order;
import cz.cvut.fel.nss.ordercommandservice.model.OrderItem;
import cz.cvut.fel.nss.ordercommandservice.pipeline.OrderPipeline;
import cz.cvut.fel.nss.ordercommandservice.pipeline.OrderProcessingException;
import cz.cvut.fel.nss.ordercommandservice.repository.OrderRepository;
import cz.cvut.fel.nss.ordercommandservice.event.OrderEvent;
import cz.cvut.fel.nss.ordercommandservice.event.OrderEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderPipeline technicalValidationFilter;
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    @Transactional
    public Order createOrder(CreateOrderRequest request) throws OrderProcessingException {
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setShippingAddress(request.getShippingAddress());
        order.setBillingAddress(request.getBillingAddress());

        var items = request.getItems().stream()
                .map(itemRequest -> {
                    OrderItem item = new OrderItem();
                    item.setProductId(itemRequest.getProductId());
                    item.setQuantity(itemRequest.getQuantity());
                    // In a real application, we would fetch the price from a product service
                    item.setUnitPrice(BigDecimal.TEN); // Placeholder price
                    item.calculateTotalPrice();
                    return item;
                })
                .collect(Collectors.toList());
        // Set parent order reference after creation
        items.forEach(item -> item.setOrder(order));
        order.setItems(items);

        // Calculate total amount
        BigDecimal totalAmount = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalAmount(totalAmount);

        // Process through pipeline
        Order processedOrder = technicalValidationFilter.process(order);

        // Save to database
        Order savedOrder = orderRepository.save(processedOrder);

        // Publish event using DTO
        OrderEvent event = OrderEventMapper.toOrderEvent(savedOrder);
        kafkaTemplate.send("order-events", savedOrder.getId().toString(), event);

        return savedOrder;
    }
} 