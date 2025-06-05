package cz.cvut.fel.nss.ordercommandservice.pipeline;

import cz.cvut.fel.nss.ordercommandservice.model.Order;
import cz.cvut.fel.nss.ordercommandservice.model.OrderItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TechnicalValidationFilter implements OrderPipeline {
    
    @Override
    public Order process(Order order) throws OrderProcessingException {
        validateCustomerId(order);
        validateOrderItems(order);
        validateAddresses(order);
        validateTotalAmount(order);
        return order;
    }

    private void validateCustomerId(Order order) throws OrderProcessingException {
        if (order.getCustomerId() == null || order.getCustomerId().trim().isEmpty()) {
            throw new OrderProcessingException("Customer ID is required");
        }
    }

    private void validateOrderItems(Order order) throws OrderProcessingException {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new OrderProcessingException("Order must contain at least one item");
        }

        for (OrderItem item : order.getItems()) {
            if (item.getProductId() == null || item.getProductId().trim().isEmpty()) {
                throw new OrderProcessingException("Product ID is required for all items");
            }
            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new OrderProcessingException("Quantity must be positive for all items");
            }
            if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new OrderProcessingException("Unit price must be positive for all items");
            }
        }
    }

    private void validateAddresses(Order order) throws OrderProcessingException {
        if (order.getShippingAddress() == null || order.getShippingAddress().trim().isEmpty()) {
            throw new OrderProcessingException("Shipping address is required");
        }
        if (order.getBillingAddress() == null || order.getBillingAddress().trim().isEmpty()) {
            throw new OrderProcessingException("Billing address is required");
        }
    }

    private void validateTotalAmount(Order order) throws OrderProcessingException {
        BigDecimal calculatedTotal = order.getItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (order.getTotalAmount() == null || 
            order.getTotalAmount().compareTo(calculatedTotal) != 0) {
            throw new OrderProcessingException("Total amount does not match sum of item totals");
        }
    }
} 