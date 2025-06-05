# Order Management System

A scalable, resilient, and modular e-commerce backend implementing CQRS, Event-Driven Architecture, and microservices patterns.

## Architecture Overview

The system consists of the following microservices:

- **Order Command Service**: Handles write operations for orders
- **Order Query Service**: Handles read operations via GraphQL
- **Inventory Service**: Manages product stock levels
- **Invoice Service**: Generates order invoices
- **Auth Service**: Manages authentication and authorization

## Technologies Used

- Java 17
- Spring Boot 3.2.3
- Spring Cloud
- Apache Kafka
- PostgreSQL
- Elasticsearch
- Hazelcast
- GraphQL
- OAuth2/JWT
- Docker

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose
- PostgreSQL
- Apache Kafka
- Elasticsearch

### Building the Project

```bash
mvn clean install
```

### Running the Services

Each service can be run independently using:

```bash
cd <service-name>
mvn spring-boot:run
```

Or using Docker Compose:

```bash
docker-compose up -d
```

## Service Endpoints

### Order Command Service
- POST /api/orders - Create new order
- PUT /api/orders/{id} - Update order
- DELETE /api/orders/{id} - Delete order

### Order Query Service
- GraphQL endpoint: /graphql

### Inventory Service
- GET /api/inventory/{productId} - Check product availability
- PUT /api/inventory/{productId} - Update stock level

### Invoice Service
- POST /api/invoices - Generate invoice

### Auth Service
- POST /oauth/token - Get access token
- POST /oauth/check_token - Validate token

## Architecture Patterns

1. **CQRS**: Separate command and query services
2. **Event-Driven**: Kafka for inter-service communication
3. **Pipes and Filters**: Order processing pipeline
4. **Microservices**: Independent, loosely coupled services
5. **Cache-Aside**: Hazelcast caching in query service

## Security

The system uses OAuth2 with JWT tokens for authentication and authorization. All service-to-service communication is secured. 