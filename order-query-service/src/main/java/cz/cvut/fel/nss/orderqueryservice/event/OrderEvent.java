package cz.cvut.fel.nss.orderqueryservice.event;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderEvent {
    private UUID id;
    private String customerId;
    private List<OrderItemEvent> items;
    private BigDecimal totalAmount;
    private String status;
    private String shippingAddress;
    private String billingAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Data
class OrderItemEvent {
    private String productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
} 