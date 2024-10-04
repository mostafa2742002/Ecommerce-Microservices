# E-Commerce Microservices Application

## Project Overview

This project is a comprehensive e-commerce application built using a microservices architecture. It consists of multiple services that work together to provide a full-fledged online shopping experience, including user management, product catalog, shopping cart, order processing, inventory management, and payment handling.

## Architecture

The application is composed of the following microservices:

1. Config Service (Port: 8071)
2. Eureka Service (Port: 8070)
3. Gateway Service (Port: 8072)
4. Authorization Service (Port: 8097)
5. User Service (Port: 8080)
6. Cart Service (Port: 8081)
7. Order Service (Port: 8082)
8. Product Service (Port: 8083)
9. Inventory Service (Port: 8084)
10. Payment Service (Port: 8085)

Additionally, the project includes:
- MySQL database (Port: 3307)
- RabbitMQ message broker (Ports: 5672, 15672)

## Prerequisites

To run this application, you need to have the following installed on your system:

- Docker
- Docker Compose

## Getting Started

1. Clone the repository containing the Docker Compose file.

2. Navigate to the project directory.

3. Run the following command to start all services:

   ```
   docker-compose up -d
   ```

   This command will download the necessary Docker images and start all the services defined in the Docker Compose file.

4. Wait for all services to start and become healthy. You can monitor the status using:

   ```
   docker-compose ps
   ```

5. Once all services are up and running, you can access the application through the Gateway Service at `http://localhost:8072`.

## Service Details

### Config Service
- Image: sasa274/ecommerce-configservice:v4
- Port: 8071
- Purpose: Centralized configuration management for all microservices.

### Eureka Service
- Image: sasa274/ecommerce-eurekaservice:v4
- Port: 8070
- Purpose: Service discovery and registration.

### Gateway Service
- Image: sasa274/ecommerce-gatewayservice:v4
- Port: 8072
- Purpose: API Gateway for routing requests to appropriate microservices.

### Authorization Service
- Image: sasa274/ecommerce-authorizationservice:v4
- Port: 8097
- Purpose: Handles user authentication and authorization.

### User Service
- Image: sasa274/ecommerce-userservice:v4
- Port: 8080
- Purpose: Manages user accounts and profiles.

### Cart Service
- Image: sasa274/ecommerce-cartservice:v4
- Port: 8081
- Purpose: Handles shopping cart operations.

### Order Service
- Image: sasa274/ecommerce-orderservice:v4
- Port: 8082
- Purpose: Manages order creation and processing.

### Product Service
- Image: sasa274/ecommerce-productservice:v4
- Port: 8083
- Purpose: Manages product catalog and information.

### Inventory Service
- Image: sasa274/ecommerce-inventoryservice:v4
- Port: 8084
- Purpose: Tracks product inventory and stock levels.

### Payment Service
- Image: sasa274/ecommerce-paymentservice:v4
- Port: 8085
- Purpose: Handles payment processing for orders.

## Gateway Service Details

The Gateway Service acts as the entry point for the e-commerce application, routing requests to appropriate microservices and providing essential features like authentication, circuit breaking, and retry mechanisms.

### Key Features

1. **Routing**: The Gateway routes requests to different microservices based on the path. All routes are prefixed with `/e-commerce/` followed by the service name.

2. **Path Rewriting**: The Gateway rewrites the paths to remove the `/e-commerce/{servicename}/` prefix before forwarding the request to the target service.

3. **Circuit Breaker**: Implemented using Resilience4j, each service route has its own circuit breaker configuration with a fallback URI.

4. **Retry Mechanism**: GET requests are automatically retried up to 3 times with exponential backoff.

5. **Token Relay**: The Gateway relays authentication tokens to the microservices, ensuring secure communication.

6. **Load Balancing**: Requests are load-balanced across service instances using the "lb" scheme in the route URIs.

### Configured Routes

The Gateway is configured to route requests to the following services:

- Cart Service: `/e-commerce/cartservice/**`
- Inventory Service: `/e-commerce/inventoryservice/**`
- Order Service: `/e-commerce/orderservice/**`
- Payment Service: `/e-commerce/paymentservice/**`
- Product Service: `/e-commerce/productservice/**`
- User Service: `/e-commerce/userservice/**`

### Circuit Breaker Configuration

Each service has its own named circuit breaker:

- Cart Service: `cartcircuitbreaker`
- Inventory Service: `inventorycircuitbreaker`
- Order Service: `ordercircuitbreaker`
- Payment Service: `paymentcircuitbreaker`
- Product Service: `productcircuitbreaker`
- User Service: `usercircuitbreaker`

The default circuit breaker configuration includes a 4-second timeout.

### Security Configuration

The Gateway implements the following security measures:

- CORS is enabled.
- All actuator endpoints are publicly accessible.
- OAuth2 login is configured.
- The Gateway acts as an OAuth2 Resource Server with JWT authentication.

### Usage

To access a specific service through the Gateway, use the following URL pattern:

```
http://localhost:8072/e-commerce/{servicename}/{endpoint}
```

For example, to access the user service:

```
http://localhost:8072/e-commerce/userservice/users
```

## Inter-Service Communication

The microservices in this application communicate with each other using Spring Cloud OpenFeign. This allows for declarative REST client definition and simplifies inter-service calls.

### Feign Clients

Each service that needs to communicate with another service defines a Feign client interface. For example, the Inventory Service communicates with the Product Service using the following Feign client:

```java
@FeignClient(name = "PRODUCTSERVICE", fallback = ProductFallback.class)
public interface ProductFeignClient {

    @GetMapping(value = "/api/product/{id}/check", consumes = "application/json")
    public Boolean checkProduct(Integer id);

}
```

### Fallback Mechanisms

To ensure resilience in case of service failures, fallback mechanisms are implemented for Feign clients. These fallbacks provide default behavior when the called service is unavailable. For example:

```java
@Component
public class ProductFallback implements ProductFeignClient {

    @Override
    public Boolean checkProduct(Integer id) {
        return false;
    }

}
```

### Retry and Circuit Breaker

In addition to Feign client fallbacks, the services also implement retry mechanisms and circuit breakers for their own endpoints. This is achieved using the `@Retry` annotation from Spring Cloud Circuit Breaker. For example:

```java
@Retry(name = "retryService", fallbackMethod = "getContactInfoFallback")
@GetMapping("/contact-info")
public ResponseEntity<InventoryContactInfoDto> getContactInfo() {
    // Method implementation
}

public ResponseEntity<InventoryContactInfoDto> getContactInfoFallback(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new InventoryContactInfoDto());
}
```

This configuration ensures that if the `getContactInfo` method fails, it will be retried according to the `retryService` configuration. If all retries fail, the `getContactInfoFallback` method will be called.

### API Documentation

The services use Swagger (OpenAPI) for API documentation. Each endpoint is annotated with `@Operation` and `@ApiResponses` to provide clear documentation of the API's functionality and possible response codes.

## Resilience Patterns

The application implements several resilience patterns to ensure high availability and fault tolerance:

1. **Circuit Breaker**: Prevents cascading failures by stopping calls to a service that is failing repeatedly.
2. **Retry**: Automatically retries failed calls to handle transient faults.
3. **Fallback**: Provides alternative behavior when a service call fails, ensuring the application can continue to function.
4. **Load Balancing**: Distributes requests across multiple instances of a service to improve performance and availability.

These patterns work together to create a robust, fault-tolerant system that can handle failures gracefully and maintain service availability.

## Database

The application uses MySQL as its database:
- Image: mysql:latest
- Port: 3307 (mapped to 3306 inside the container)
- Environment variables:
  - MYSQL_DATABASE: mydb
  - MYSQL_ROOT_PASSWORD: root

## Message Broker

RabbitMQ is used as the message broker for inter-service communication:
- Image: rabbitmq:3.13-management
- Ports: 
  - 5672 (AMQP protocol)
  - 15672 (Management UI)

## Networking

All services are connected through a custom bridge network named `ecommerce`.

## Health Checks

Each service includes health checks to ensure they are running correctly before being marked as ready. The Gateway Service depends on the health of all other services before starting.

## Resource Limits

Most services have a memory limit of 700MB to prevent resource exhaustion.

## Configuration

Services are configured to use the Config Service for centralized configuration management. The `SPRING_CONFIG_IMPORT` environment variable is set to point to the Config Service URL.


# E-Commerce Microservices Application

[Previous content remains unchanged]

## Configuration Management

This application uses a centralized configuration management approach, leveraging Spring Cloud Config for externalized configuration.

### Configuration Repository

All configuration files for the microservices are stored in a dedicated GitHub repository:

[https://github.com/mostafa2742002/Ecommerce-Microservices-config](https://github.com/mostafa2742002/Ecommerce-Microservices-config)

### Environment-Specific Configurations

Each service has two configuration files:

1. A default configuration file for the development environment
2. A production-specific configuration file

This setup allows for environment-specific settings, making it easier to manage different configurations for development, testing, and production environments.

### Dynamic Configuration Updates

The application implements a dynamic configuration update mechanism:

1. When changes are pushed to the configuration repository on GitHub, a webhook is triggered.
2. The webhook sends a request to the Config Server.
3. The Config Server then refreshes the configurations for all services.

This mechanism ensures that configuration changes can be applied to the running services without requiring a restart, enabling seamless updates to application settings.

### Usage

To make use of this centralized configuration:

1. Developers should make configuration changes in the GitHub repository mentioned above.
2. For environment-specific changes, modify the appropriate file (default or production).
3. After pushing changes to the repository, the services will automatically update their configurations.

### Benefits

- Centralized management of all service configurations
- Easy differentiation between development and production settings
- Dynamic updates without service restarts
- Version control and history of configuration changes

### Caution

When making changes to the configuration:

- Ensure that the changes are appropriate for the target environment.
- Test configuration changes in a non-production environment before applying them to production.
- Be aware that some configuration changes might require service restarts to take effect fully.

## Service Discovery

All microservices are configured to register with Eureka for service discovery. The `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` environment variable is set to the Eureka Service URL.

## Security

The Authorization Service is configured with OAuth2 settings. Make sure to review and adjust the client IDs and secrets as needed for your production environment.

## Troubleshooting

If you encounter any issues:

1. Check the logs of the specific service:
   ```
   docker-compose logs [service_name]
   ```

2. Ensure all services are healthy:
   ```
   docker-compose ps
   ```

3. Restart a specific service:
   ```
   docker-compose restart [service_name]
   ```

4. If problems persist, try rebuilding the images:
   ```
   docker-compose build --no-cache
   docker-compose up -d
   ```
