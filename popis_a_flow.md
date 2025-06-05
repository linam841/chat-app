# Order Management System – Final Architecture

## 📜 Project Overview

This Order Management System is designed as a **scalable, resilient, and modular e-commerce backend**, implementing key software architecture principles:

### ✅ Included Styles & Concepts:

* **Microservices architecture**
* **CQRS (Command Query Responsibility Segregation)**
* **Event-driven communication (Kafka)**
* **Pipes and Filters** (order validation and enrichment pipeline)
* **Spring Boot** across services
* **Security with OAuth2**
* **Caching (Hazelcast)** and **Search (Elasticsearch)**
* **Use of 5+ Design Patterns** (see below)

---

## ⚙️ Microservices Breakdown

### 1. 🔹 `order-command-service`

Handles all **write operations** related to orders.

* REST API for `POST`, `PUT`, `DELETE /orders`
* **Pipes and Filters** pipeline:

  * `TechnicalValidationFilter`
  * `InventorySyncFilter`
  * `BusinessValidationFilter`
  * `OrderEnrichmentFilter`
  * `InvoiceGenerationFilter`
* Saves to PostgreSQL
* Emits `OrderCreatedEvent`, `OrderUpdatedEvent`, etc. to Kafka

### 2. 🔹 `order-query-service`

Handles **read operations** via GraphQL.

* Exposes GraphQL API (`/graphql`)
* Listens to Kafka topics and:

  * Updates **Elasticsearch** for fast search
  * Caches hot data in **Hazelcast**
* Handles queries using cache first → fallback to Elasticsearch

### 3. 🔹 `inventory-service`

Validates and manages stock levels.

* REST API for checking availability
* Queried during order creation by `order-command-service`
* Owns its own product/stock database

### 4. 🔹 `invoice-service`

Generates PDF or JSON invoices during order creation.

* REST endpoint or Kafka listener
* Uses libraries like iText or Apache PDFBox
* Saves invoices to storage or attaches them to order object

### 5. 🔹 `auth-service` (or Keycloak)

Manages OAuth2-based authentication.

* Issues JWT tokens
* Enforces access control in all APIs (read, write)

---

## 🔄 Flow of Application

### A. 🛒 Order Creation

```
User → order-command-service (POST /orders)
  ↳ OAuth2 authentication (auth-service)
  ↳ Pipes and Filters:
     - Validate data
     - Check stock (via inventory-service)
     - Apply business rules
     - Add timestamps, IDs
     - Generate invoice (via invoice-service)
  ↳ Save to PostgreSQL
  ↳ Emit OrderCreatedEvent → Kafka
```

### B. 📅 Event Consumption

```
Kafka → order-query-service:
  ↳ Update Elasticsearch index
  ↳ Update Hazelcast cache
```

### C. 🔍 Order Query

```
User → order-query-service (GraphQL query)
  ↳ OAuth2 authentication
  ↳ Check cache → fallback to ES
  ↳ Return result
```

### D. ✏️ Update / 🗑️ Delete Order

```
User → order-command-service
  ↳ Validate + process
  ↳ Update DB
  ↳ Emit OrderUpdatedEvent / OrderDeletedEvent
  ↳ order-query-service updates ES/cache
```

---

## 🧱 Basic Project Structure

Each service is a standalone Spring Boot project:

```
order-command-service/
├— controller/      → REST API
├— service/         → Business logic
├— pipeline/        → Pipes & Filters (validators, enrichers)
├— kafka/           → Kafka producer
├— model/           → JPA entities
├— repository/      → Spring Data JPA
├— config/          → Security, Kafka, DB
└— resources/       → application.properties

order-query-service/
├— graphql/         → GraphQL API
├— listener/        → Kafka listener
├— cache/           → Hazelcast access
├— search/          → Elasticsearch access
├— config/          → Security, Kafka
└— resources/

inventory-service/
├— controller/      → Stock check endpoints
├— service/         → Inventory rules
├— model/           → Product entities
└— repository/

invoice-service/
├— controller/      → Invoice generation endpoint (optional)
├— service/         → PDF/JSON generation logic

auth-service/
└— (Optional: config for Keycloak or Spring Security Resource Server)
```

---

## ✅ Where Project Requirements Are Met

| Requirement                           | How It’s Satisfied                                              |
| ------------------------------------- | --------------------------------------------------------------- |
| **Use of Spring Boot**                | All services use Spring Boot                                    |
| **CQRS architecture**                 | `order-command-service` (write) vs `order-query-service` (read) |
| **Microservices**                     | 5 services: command, query, inventory, invoice, auth            |
| **Database used**                     | PostgreSQL in command service, separate DBs per service         |
| **Cache**                             | Hazelcast in query service                                      |
| **Kafka / messaging**                 | Used between command and query service                          |
| **Security**                          | OAuth2 (via Spring Security or Keycloak)                        |
| **Interceptors**                      | Logging interceptor in write service                            |
| **Service tech (REST/GraphQL/Kafka)** | REST (command, inventory), GraphQL (query), Kafka (events)      |
| **Elasticsearch**                     | Used for fast search in `order-query-service`                   |
| **Pipes and Filters**                 | Implemented in command service before saving order              |
| **5+ Design Patterns**                | ✅                                                               |

* Strategy (Filters)
* Chain of Responsibility (Pipeline)
* Singleton (Cache configs)
* Observer (Kafka)
* Builder (Invoice/PDF)
* DTO + Factory (Events) | | **2 use cases per team member** | Can be split across create, update, delete, view, export, validate |

---
