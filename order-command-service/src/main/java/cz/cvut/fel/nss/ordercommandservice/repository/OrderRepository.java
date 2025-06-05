package cz.cvut.fel.nss.ordercommandservice.repository;

import cz.cvut.fel.nss.ordercommandservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Additional query methods can be added here
} 