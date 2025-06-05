package cz.cvut.fel.nss.orderqueryservice.kafka;

import cz.cvut.fel.nss.orderqueryservice.event.OrderEvent;
import cz.cvut.fel.nss.orderqueryservice.model.OrderDocument;
import cz.cvut.fel.nss.orderqueryservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "order-events", groupId = "order-query-service")
    @CacheEvict(value = "orders", allEntries = true)
    public void handleOrderEvent(OrderEvent event) {
        OrderDocument document = new OrderDocument();
        document.setId(event.getId().toString());
        document.setCustomerId(event.getCustomerId());
        document.setTotalAmount(event.getTotalAmount());
        document.setStatus(event.getStatus());
        document.setShippingAddress(event.getShippingAddress());
        document.setBillingAddress(event.getBillingAddress());
        document.setCreatedAt(event.getCreatedAt());
        document.setUpdatedAt(event.getUpdatedAt());

        document.setItems(event.getItems().stream()
                .map(item -> {
                    OrderDocument.OrderItemDocument itemDoc = new OrderDocument.OrderItemDocument();
                    itemDoc.setProductId(item.getProductId());
                    itemDoc.setQuantity(item.getQuantity());
                    itemDoc.setUnitPrice(item.getUnitPrice());
                    itemDoc.setTotalPrice(item.getTotalPrice());
                    return itemDoc;
                })
                .collect(Collectors.toList()));

        orderRepository.save(document);
    }
} 