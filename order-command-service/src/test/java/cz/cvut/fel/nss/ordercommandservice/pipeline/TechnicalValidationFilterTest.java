package cz.cvut.fel.nss.ordercommandservice.pipeline;

import cz.cvut.fel.nss.ordercommandservice.model.Order;
import cz.cvut.fel.nss.ordercommandservice.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class TechnicalValidationFilterTest {

    private TechnicalValidationFilter filter;
    private Order validOrder;

    @BeforeEach
    void setUp() {
        filter = new TechnicalValidationFilter();
        
        // Set up a valid order
        validOrder = new Order();
        validOrder.setCustomerId("customer123");
        validOrder.setShippingAddress("123 Ship St");
        validOrder.setBillingAddress("123 Bill St");
        
        OrderItem item = new OrderItem();
        item.setProductId("product123");
        item.setQuantity(2);
        item.setUnitPrice(BigDecimal.TEN);
        item.setOrder(validOrder);
        item.calculateTotalPrice(); // Calculate total price
        
        validOrder.setItems(Collections.singletonList(item));
        validOrder.setTotalAmount(BigDecimal.valueOf(20)); // 2 * 10
    }

    @Test
    void process_ValidOrder_ShouldPass() {
        assertDoesNotThrow(() -> filter.process(validOrder));
    }

    @Test
    void process_NullCustomerId_ShouldThrowException() {
        validOrder.setCustomerId(null);
        OrderProcessingException exception = assertThrows(
            OrderProcessingException.class,
            () -> filter.process(validOrder)
        );
        assertEquals("Customer ID is required", exception.getMessage());
    }

    @Test
    void process_EmptyCustomerId_ShouldThrowException() {
        validOrder.setCustomerId("  ");
        OrderProcessingException exception = assertThrows(
            OrderProcessingException.class,
            () -> filter.process(validOrder)
        );
        assertEquals("Customer ID is required", exception.getMessage());
    }

    @Test
    void process_NoItems_ShouldThrowException() {
        validOrder.setItems(Collections.emptyList());
        OrderProcessingException exception = assertThrows(
            OrderProcessingException.class,
            () -> filter.process(validOrder)
        );
        assertEquals("Order must contain at least one item", exception.getMessage());
    }

    @Test
    void process_InvalidTotalAmount_ShouldThrowException() {
        validOrder.setTotalAmount(BigDecimal.ONE);
        OrderProcessingException exception = assertThrows(
            OrderProcessingException.class,
            () -> filter.process(validOrder)
        );
        assertEquals("Total amount does not match sum of item totals", exception.getMessage());
    }

    @Test
    void process_MissingShippingAddress_ShouldThrowException() {
        validOrder.setShippingAddress(null);
        OrderProcessingException exception = assertThrows(
            OrderProcessingException.class,
            () -> filter.process(validOrder)
        );
        assertEquals("Shipping address is required", exception.getMessage());
    }

    @Test
    void process_MissingBillingAddress_ShouldThrowException() {
        validOrder.setBillingAddress(null);
        OrderProcessingException exception = assertThrows(
            OrderProcessingException.class,
            () -> filter.process(validOrder)
        );
        assertEquals("Billing address is required", exception.getMessage());
    }
} 