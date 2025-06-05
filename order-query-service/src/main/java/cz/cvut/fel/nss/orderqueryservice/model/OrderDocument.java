package cz.cvut.fel.nss.orderqueryservice.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(indexName = "orders")
public class OrderDocument {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String customerId;

    @Field(type = FieldType.Nested)
    private List<OrderItemDocument> items;

    @Field(type = FieldType.Double)
    private BigDecimal totalAmount;

    @Field(type = FieldType.Keyword)
    private String status;

    @Field(type = FieldType.Text)
    private String shippingAddress;

    @Field(type = FieldType.Text)
    private String billingAddress;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date)
    private LocalDateTime updatedAt;

    @Data
    public static class OrderItemDocument {
        @Field(type = FieldType.Keyword)
        private String productId;

        @Field(type = FieldType.Integer)
        private Integer quantity;

        @Field(type = FieldType.Double)
        private BigDecimal unitPrice;

        @Field(type = FieldType.Double)
        private BigDecimal totalPrice;
    }
} 