package cz.cvut.fel.nss.ordercommandservice.pipeline;

import cz.cvut.fel.nss.ordercommandservice.model.Order;
 
public interface OrderPipeline {
    Order process(Order order) throws OrderProcessingException;
} 