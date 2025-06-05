package cz.cvut.fel.nss.orderqueryservice.graphql;

import cz.cvut.fel.nss.orderqueryservice.model.OrderDocument;
import cz.cvut.fel.nss.orderqueryservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderResolver {
    private final OrderService orderService;

    @QueryMapping
    public OrderDocument order(@Argument String id) {
        return orderService.findById(id);
    }

    @QueryMapping
    public List<OrderDocument> ordersByCustomer(@Argument String customerId) {
        return orderService.findByCustomerId(customerId);
    }

    @QueryMapping
    public List<OrderDocument> ordersByStatus(@Argument String status) {
        return orderService.findByStatus(status);
    }

    @QueryMapping
    public List<OrderDocument> ordersByCustomerAndStatus(
            @Argument String customerId,
            @Argument String status) {
        return orderService.findByCustomerIdAndStatus(customerId, status);
    }
} 