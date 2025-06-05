package cz.cvut.fel.nss.orderqueryservice.service;

import cz.cvut.fel.nss.orderqueryservice.model.OrderDocument;
import cz.cvut.fel.nss.orderqueryservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Cacheable(value = "orders", key = "#id")
    public OrderDocument findById(String id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Cacheable(value = "orders", key = "'customer_' + #customerId")
    public List<OrderDocument> findByCustomerId(String customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Cacheable(value = "orders", key = "'status_' + #status")
    public List<OrderDocument> findByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Cacheable(value = "orders", key = "'customer_' + #customerId + '_status_' + #status")
    public List<OrderDocument> findByCustomerIdAndStatus(String customerId, String status) {
        return orderRepository.findByCustomerIdAndStatus(customerId, status);
    }
} 