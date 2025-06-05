package cz.cvut.fel.nss.ordercommandservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.fel.nss.ordercommandservice.config.TestSecurityConfig;
import cz.cvut.fel.nss.ordercommandservice.dto.CreateOrderRequest;
import cz.cvut.fel.nss.ordercommandservice.dto.OrderItemRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Import(TestSecurityConfig.class)
class OrderControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine")
    )
            .withDatabaseName("orderdb_test")
            .withUsername("admin")
            .withPassword("admin");

    @Container
    static KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.5.1")
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_ValidRequest_ShouldReturnCreated() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId("customer123");
        request.setShippingAddress("123 Ship St");
        request.setBillingAddress("123 Bill St");

        OrderItemRequest item = new OrderItemRequest(); // Corrected: This was missing from the original context but present in user's latest attachment
        item.setProductId("product123");
        item.setQuantity(2);
        request.setItems(Collections.singletonList(item));

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void createOrder_InvalidRequest_ShouldReturnBadRequest() throws Exception {
        CreateOrderRequest request = new CreateOrderRequest();
        // Missing required fields

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
} 