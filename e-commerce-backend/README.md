# E-Commerce Backend - Spring Boot 3.5

Complete e-commerce backend with Spring Boot, JPA/Hibernate, Spring Batch, Quartz Scheduler, Kafka for email notifications.

## Features

### Core Entities & Relations
- **User**: OneToMany -> Address, OneToMany -> Order, ManyToMany -> Product (Wishlist)
- **Address**: ManyToOne -> User
- **Product**: for products catalog
- **Order**: ManyToOne -> User, OneToMany -> OrderItem
- **OrderItem**: connects Order and Product
- **Wishlist**: ManyToMany relation between User and Product

### Scheduled Order Export
- **Spring Batch**: Daily export all orders to CSV
- **Quartz Scheduler**: Runs automatically every night at 1:00 AM (configurable cron)
- **Kafka**: After export completes, send notification message to Kafka
- **Email Service**: Consumes Kafka message and sends email with CSV attachment

## Tech Stack
- Spring Boot 3.5
- Spring Data JPA + Hibernate
- MySQL Database
- Spring Batch
- Quartz Scheduler
- Spring Kafka
- Java Mail
- Lombok
- MapStruct (DTO -> Entity mapping)

## Prerequisites
- Java 17+
- Maven 3.6+
- MySQL running locally
- Kafka running locally on `localhost:9092`

## Configuration
All configuration in `src/main/resources/application.properties`:
```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=root

# Scheduler: run every night at 1AM
app.export.cron=0 0 1 * * ?

# Kafka
app.kafka.email-topic=order-export-email-notification

# Email (configure your SMTP)
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## Build & Run

### Local
```bash
mvn clean install
mvn spring-boot:run
```

### Docker Compose
Starts MySQL, Kafka, Zookeeper and the E-Commerce app all in one go:
```bash
# Build image and start all containers
docker-compose up --build -d

# Check logs
docker-compose logs -f ecommerce-app

# Stop everything
docker-compose down
```

## REST API Endpoints

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/users` | Create user |
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| PUT | `/api/users/{id}` | Update user |
| DELETE | `/api/users/{id}` | Delete user |

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/products` | Create product |
| GET | `/api/products` | Get all products |
| GET | `/api/products/{id}` | Get product by ID |
| GET | `/api/products/search` | Search products by name |
| PUT | `/api/products/{id}` | Update product |
| DELETE | `/api/products/{id}` | Delete product |

### Orders
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Create order |
| GET | `/api/orders` | Get all orders |
| GET | `/api/orders/{id}` | Get order by ID |
| GET | `/api/orders/user/{userId}` | Get orders by user |
| PATCH | `/api/orders/{id}/status` | Update order status |

### Wishlist
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/wishlist/{userId}` | Get user wishlist |
| POST | `/api/wishlist/{userId}/products/{productId}` | Add product to wishlist |
| DELETE | `/api/wishlist/{userId}/products/{productId}` | Remove product from wishlist |
| DELETE | `/api/wishlist/{userId}` | Clear wishlist |

## Database Schema
JPA automatically creates tables:
- `users` - user accounts
- `addresses` - user addresses
- `products` - product catalog
- `orders` - customer orders
- `order_items` - line items in orders
- `user_wishlist` - join table for User <-> Product many-to-many (wishlist)
- `QRTZ_*` - Quartz scheduler tables (auto created)
- `BATCH_*` - Spring Batch tables (auto created)

## Project Structure
```
src/main/java/com/example/ecommerce/
├── ECommerceApplication.java      # Main entry
├── config/
│   ├── BatchConfig.java           # Spring Batch job configuration
│   ├── KafkaConfig.java           # Kafka producer/consumer config
│   └── QuartzConfig.java          # Quartz scheduler configuration
├── entity/
│   ├── User.java
│   ├── Address.java
│   ├── Product.java
│   ├── Order.java
│   └── OrderItem.java
├── dto/
│   ├── UserRequest.java
│   ├── ProductRequest.java
│   ├── OrderRequest.java
│   └── OrderExportCompletedNotification.java  # Kafka message for email
├── mapper/                         # MapStruct mappers for DTO-Entity mapping
│   ├── UserMapper.java
│   └── ProductMapper.java
├── repository/                     # Spring Data JPA repositories
├── service/
│   ├── UserService.java
│   ├── ProductService.java
│   ├── OrderService.java
│   ├── WishlistService.java
│   └── EmailService.java          # Consumes from Kafka, sends emails
├── batch/
│   ├── OrderExportCsvRecord.java  # CSV record for export
│   └── OrderExportItemProcessor.java  # Batch item processor
└── scheduler/
    └── OrderExportJob.java        # Quartz job to launch Spring Batch
```

## Daily Order Export Flow
1. **Quartz** triggers job at 1:00 AM every night
2. **OrderExportJob** launches Spring Batch `orderExportJob`
3. Spring Batch reads all orders from database
4. Processes orders -> CSV records
5. Writes to CSV file in `exports/` directory
6. After job completes, job listener sends `OrderExportCompletedNotification` to **Kafka** topic
7. **EmailService** listens to Kafka topic, sends email with CSV attachment

## Notes
- Passwords are stored in plain text in this demo - in production use BCrypt password hashing with Spring Security
- Configure your SMTP credentials in `application.properties` for email to work
- Kafka must be running for the email notification part
