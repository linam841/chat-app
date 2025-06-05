package cz.cvut.fel.nss.ordercommandservice.model;

public enum OrderStatus {
    CREATED,
    VALIDATED,
    INVENTORY_CONFIRMED,
    PAYMENT_PENDING,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUNDED
} 