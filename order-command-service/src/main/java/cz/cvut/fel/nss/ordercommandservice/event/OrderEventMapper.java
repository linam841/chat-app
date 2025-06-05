package cz.cvut.fel.nss.ordercommandservice.event;

import cz.cvut.fel.nss.ordercommandservice.model.Order;
import cz.cvut.fel.nss.ordercommandservice.model.OrderItem;

import java.util.stream.Collectors;

public class OrderEventMapper {
    public static OrderEvent toOrderEvent(Order order) {
        OrderEvent event = new OrderEvent();
        event.setId(order.getId());
        event.setCustomerId(order.getCustomerId());
        event.setTotalAmount(order.getTotalAmount());
        event.setStatus(order.getStatus().name());
        event.setShippingAddress(order.getShippingAddress());
        event.setBillingAddress(order.getBillingAddress());
        event.setCreatedAt(order.getCreatedAt());
        event.setUpdatedAt(order.getUpdatedAt());
        event.setItems(order.getItems().stream().map(OrderEventMapper::toOrderItemEvent).collect(Collectors.toList()));
        return event;
    }

    private static OrderEvent.OrderItemEvent toOrderItemEvent(OrderItem item) {
        OrderEvent.OrderItemEvent itemEvent = new OrderEvent.OrderItemEvent();
        itemEvent.setProductId(item.getProductId());
        itemEvent.setQuantity(item.getQuantity());
        itemEvent.setUnitPrice(item.getUnitPrice());
        itemEvent.setTotalPrice(item.getTotalPrice());
        return itemEvent;
    }
} 