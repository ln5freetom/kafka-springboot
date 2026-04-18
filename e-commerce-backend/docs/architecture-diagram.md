# E-Commerce Project Architecture Diagram

## Full Stack Architecture

```
┌─────────────────────────────────────────────────────────────────────────┐
│                        Client (Browser)                                  │
└─────────────────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP (Angular)
                              ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                     Angular 17 Frontend (port 4200)                     │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │  Products  │  │   Users    │  │   Orders   │  │  Wishlist  │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  │
│                                                         │                 │
│                                                      Proxy → API          │
└─────────────────────────────────────────────────────────────────────────┘
                              │
                              │ REST API
                              ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                   Spring Boot 3.5 Backend (port 8080)                   │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐  │
│  │ Controller │  │  Service   │  │  Repository │  │  Entity    │  │
│  └─────────────┘  └─────────────┘  └─────────────┘  └─────────────┘  │
│                                                                  │       │
│                     ┌──────────────┐                     ┌──────────────┐  │
│                     │  Spring Batch │                     │  Quartz      │  │
│                     │  (CSV Export)│                     │  Scheduler   │  │
│                     └──────────────┘                     └──────────────┘  │
│                                              │                              │
│                                              │ Export complete             │
│                                              ▼                              │
│                     ┌──────────────────────────────────────────────┐     │
│                     │                   Kafka                      │     │
│                     │  Topic: order-export-email-notification    │     │
│                     └──────────────────────────────────────────────┘     │
│                                              │                              │
│                                              ▼                              │
│                     ┌──────────────────────────────────────────────┐     │
│                     │               EmailService                    │     │
│                     │  Send email with exported CSV attachment   │     │
│                     └──────────────────────────────────────────────┘     │
└─────────────────────────────────────────────────────────────────────────┘
                              │
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────────┐
│                            MySQL 8.0 Database                           │
│                                                                         │
│  ┌─────────┐ ┌─────────┐ ┌────────┐ ┌──────────┐ ┌─────────────┐    │
│  │ users   │ │addresses│ │products│ │ orders  │ │order_items│    │
│  └─────────┘ └─────────┘ └────────┘ └──────────┘ └─────────────┘    │
│                     user_wishlist (many-to-many join table)             │
│                     QRTZ_* (Quartz tables)  BATCH_* (Spring Batch)    │
└─────────────────────────────────────────────────────────────────────────┘
```

## Entity Relationship Diagram

```
          ┌───────────────────────────────────────────────────────────┐
          │                         User                               │
          │  - id                                                │
          │  - username (unique)                                  │
          │  - email (unique)                                   │
          │  - password                                          │
          │  - firstName, lastName                               │
          │  - createdAt, updatedAt                             │
          │                                                         │
          │  * OneToMany -> Address (user_id FK)                 │
          │  * OneToMany -> Order (user_id FK)                   │
          │  * ManyToMany -> Product (join table: user_wishlist) │
          └───────────────────────────────────────────────────────────┘
                     │
             ┌───────┘
             │
┌───────────────────────────────────────────────────────────────────────────────┐
│  Address                                    User                          │
│  - id                                        ▲                         │
│  - street                                   │                         │
│  - city                         OneToMany     │                         │
│  - state                        ManyToOne      │                         │
│  - zipCode                                    │                         │
│  - country                        user_id ─────┘                         │
│  - isDefault                                                    │
└───────────────────────────────────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────────────────────────────┐
│  Product                                 user                          │
│  - id                                        ▲                         │
│  - name                          ManyToMany                     │
│  - description                  (user_wishlist)                 │
│  - price                                                      │
│  - stock                                                       │
│  - imageUrl                                                    │
│  - createdAt                                                  │
│                              └───────────────────────────────────────────────┘
└─────────────────────────────────────────────────────────────────────────────────┘

                          ┌───────────────────────────────────────────────────┐
                          │                      Order                          │
                          │  - id                                            │
                          │  - orderNumber (unique)                        │
                          │  - status (ENUM: PENDING, PAID, SHIPPED...)│
                          │  - totalAmount                                  │
                          │  - user_id ────────────┐                        │
                          │  - shipping_address_id ─►│                        │
                          │  - createdAt                                   │
                          │                                                         │
                          │  * OneToMany -> OrderItem (order_id FK)          │
                          └───────────────────────────────────────────────────┘
                                                     │
                    ┌────────┴────────┐
                    │                 │
┌──────────────────────────────────────────────────────────────────────────────┐
│  OrderItem                                                   │
│  - id                    │                                       │
│  - quantity             │                                       │
│  - price                │                                       │
│  - order_id   ◄─────────┘                                       │
│  - product_id ◄────────────────────────────────────────────────  │
└───────────────────────────────────────────────────────────────────────────────┘
```

## Daily Order Export Flow

```
┌──────────────┐
│  Quartz      │  1:00 AM every night
│  Scheduler   │
└──────┬───────┘
       │
       ▼
┌──────────────┐
│  Spring      │
│  Batch Job   │  Reads all orders from DB
└──────┬───────┘
       │
       ▼
 ┌───────────┐
 │ Processor │  Converts Order -> OrderExportCsvRecord
 └─────┬─────┘
       │
       ▼
 ┌───────────┐
 │ Writer    │  Writes to exports/orders-export-<date>.csv
 └─────┬─────┘
       │
       ▼
┌────────────────────────┐
│ Job Completion Listener │
└───────────┬────────────┘
            │
            ▼
┌──────────────────────────────────┐
│  Send notification to Kafka topic │
└───────────┬──────────────────────┘
            │
            ▼
┌──────────────────────────────────┐
│  EmailService listens on Kafka   │
└───────────┬──────────────────────┘
            │
            ▼
┌──────────────────────────────────┐
│  Send email with CSV attachment   │
└──────────────────────────────────┘
```

## Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Frontend | Angular | 17 |
| Backend | Spring Boot | 3.5 |
| ORM | Hibernate | 6 |
| Database | MySQL | 8.0 |
| Batch Processing | Spring Batch | 5 |
| Scheduler | Quartz | |
| Messaging | Apache Kafka | |
| Email | Spring Mail | |
| DTO Mapping | MapStruct | 1.5.5 |
| CI/CD | Jenkins | |
| Code Quality | SonarQube | |
| Container | Docker + Docker Compose | |

## Project Structure

```
root/
├── e-commerce-backend/
│   ├── src/main/
│   │   ├── java/com/example/ecommerce/
│   │   │   ├── ECommerceApplication.java
│   │   │   ├── entity/              # JPA Entities
│   │   │   ├── dto/                 # DTOs
│   │   │   ├── mapper/              # MapStruct mappers
│   │   │   ├── repository/          # Spring Data JPA repositories
│   │   │   ├── service/             # Business logic
│   │   │   ├── controller/          # REST API controllers
│   │   │   ├── config/              # Batch, Kafka, Quartz config
│   │   │   ├── batch/               # Spring Batch components
│   │   │   └── scheduler/           # Quartz job
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── init.sql             # Create tables
│   │       └── test-data.sql        # Pre-populated test data
│   ├── src/test/
│   │   └── java/                    # Unit tests + integration tests
│   ├── Dockerfile
│   ├── docker-compose.yml           # MySQL + Kafka + Zookeeper + App
│   ├── Jenkinsfile                  # Jenkins CI/CD pipeline for GitHub
│   ├── sonar-project.properties      # SonarQube configuration
│   ├── SETUP-CHECKLIST.md           # Manual configuration checklist
│   └── UPGRADE-TODO.md             # JDK 8 + Spring Boot 2 upgrade guide
└── e-commerce-frontend/
    ├── src/
    │   ├── app/
    │   │   ├── components/           # Angular components
    │   │   ├── services/             # HTTP services
    │   │   └── models/               # TypeScript interfaces
    │   └── index.html
    ├── angular.json
    ├── package.json
    └── proxy.conf.json              # Proxy to backend API
```
