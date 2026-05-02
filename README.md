# 🔰 Microservices Complete Flow (Interview Example)

This repository demonstrates a complete Microservices Architecture designed to perfectly align with common interview questions. It includes a fully functional ecosystem utilizing Spring Boot, Spring Cloud, Apache Kafka, and Docker.

## 🌟 Architecture Overview

Based on your Q&A, this architecture consists of the following components:

*   **API Gateway** (`api-gateway`): Built with **Spring Cloud Gateway**. Acts as the single entry point, routing requests to the respective microservices.
*   **Service Registry** (`service-registry`): Built with **Netflix Eureka**. Microservices register themselves here so they can dynamically discover each other.
*   **Message Broker** (`kafka`): Powered by **Apache Kafka**. Facilitates asynchronous, event-driven communication (e.g., Order Service notifying Notification Service).
*   **Microservices**:
    *   `order-service`: Handles order creation (Sync REST) and publishes events to Kafka (Async). Uses an in-memory **H2** database for fast startup.
    *   `payment-service`: Simulates payment processing to demonstrate independent scaling and synchronous REST availability.
    *   `notification-service`: Consumes events from Kafka to demonstrate decoupled, event-driven architecture.

## 🛠️ Tech Stack & Tools

*   **Java 17 & Spring Boot 3.2.x**
*   **Spring Cloud Netflix Eureka** (Service Discovery)
*   **Spring Cloud Gateway** (Routing)
*   **Spring Data JPA & H2** (Database)
*   **Apache Kafka** (Event-driven Architecture)
*   **Docker & Docker Compose** (Containerization & Orchestration)

---

## 🚀 How to Run the Project

Since this is a multi-module Maven project, you must build the `.jar` files first before running `docker-compose`.

### Step 1: Build the Microservices
Ensure you have Maven installed, or use a Dockerized Maven to build the project. Open your terminal in the root directory and run:

```bash
mvn clean package -DskipTests
```

This will compile all 5 Spring Boot applications and generate `.jar` files inside their respective `target/` directories.

### Step 2: Start the Environment
Use Docker Compose to spin up Zookeeper, Kafka, and all microservices.

```bash
docker-compose up --build -d
```

*Wait a couple of minutes for all services to start and register with Eureka.*

### Step 3: Verify the Setup

1. **Eureka Dashboard:** Open `http://localhost:8761`. You should see `API-GATEWAY`, `ORDER-SERVICE`, `PAYMENT-SERVICE`, and `NOTIFICATION-SERVICE` registered.
2. **Create an Order (via Gateway):**
   ```bash
   curl -X POST -H "Content-Type: application/json" -d '{"product":"Laptop","quantity":1,"price":1500.0}' http://localhost:8080/api/orders
   ```
3. **Verify Notification (Kafka Consumer):**
   Check the logs of the Notification Service to see if it consumed the Kafka event.
   ```bash
   docker logs notification-service
   ```
   *Expected Output:* `Notification Service received message: Order created with ID: 1`

4. **Call Payment Service (via Gateway):**
   ```bash
   curl -X POST http://localhost:8080/api/payments
   ```
   *Expected Output:* `Payment processed successfully!`

---

## 🎤 Interview Cheatsheet (Mapping Code to Q&A)

Here is how you can map this codebase to the interview questions:

1. **"What are microservices / Main components?"**
   > *Answer:* Look at `docker-compose.yml`. We have independent services (`order`, `payment`, `notification`), a Service Registry (**Eureka**), an API Gateway (**Spring Cloud Gateway**), and a Message Broker (**Kafka**).

2. **"How do services communicate?"**
   > *Answer:* We use both!
   > * **Synchronous (REST):** The `api-gateway` routes HTTP requests to `order-service` and `payment-service`.
   > * **Asynchronous (Kafka):** The `order-service` produces a message to the `order-events` topic, which the `notification-service` consumes.

3. **"How do you handle Service Discovery?"**
   > *Answer:* We use **Netflix Eureka** (`service-registry`). Notice how `order-service` doesn't hardcode the IP of the database or other services; it registers with `http://service-registry:8761/eureka/`.

4. **"What is the role of the API Gateway?"**
   > *Answer:* It handles routing. In `api-gateway/src/main/resources/application.yml`, we define routes like `- Path=/api/orders/**` mapping to `lb://order-service`. The `lb://` prefix means it uses the Load Balancer to fetch the instance from Eureka.

5. **"Containers and Deployment?"**
   > *Answer:* Every service has a `Dockerfile`. We use `docker-compose.yml` to orchestrate the entire environment, making it identical across dev and production.

> **🎯 Pro Tip:** In interviews, confidently state: *"In my recent project, I built a microservices ecosystem using Spring Cloud Gateway as the API Gateway, Eureka for service discovery, and Apache Kafka for asynchronous communication between the Order and Notification services, all containerized with Docker."*

***https://www.youtube.com/watch?v=wmawYODmQU0*** 