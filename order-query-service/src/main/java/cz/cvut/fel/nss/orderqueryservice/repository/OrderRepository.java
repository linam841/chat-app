package cz.cvut.fel.nss.orderqueryservice.repository;

import cz.cvut.fel.nss.orderqueryservice.model.OrderDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends ElasticsearchRepository<OrderDocument, String> {
    List<OrderDocument> findByCustomerId(String customerId);
    List<OrderDocument> findByStatus(String status);
    List<OrderDocument> findByCustomerIdAndStatus(String customerId, String status);
} 