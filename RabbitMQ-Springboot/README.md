# Spring Boot RabbitMQ Demo

A complete demo with Spring Boot RabbitMQ **Producer** and **Consumer** applications.

## Projects

- [`producer/`](producer/) - RabbitMQ Producer application that sends JSON messages via REST API
  - Runs on port `8082`
  - Sends messages to `demo-queue`

- [`consumer/`](consumer/) - RabbitMQ Consumer application that consumes messages and exposes them via REST API
  - Runs on port `8083`
  - Stores received messages in-memory for inspection

## Quick Start

### 1. Start RabbitMQ locally (with Docker)

```bash
docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```

Management UI is available at: http://localhost:15672 (guest/guest)

### 2. Start the Consumer

```bash
cd consumer
mvn spring-boot:run
```

### 3. Start the Producer

```bash
cd producer
mvn spring-boot:run
```

### 4. Test the flow

**Send a message:**
```bash
curl http://localhost:8082/api/messages/send
```

**Send custom message:**
```bash
curl -X POST -H "Content-Type: text/plain" http://localhost:8082/api/messages -d "Hello RabbitMQ!"
```

**Check received messages in consumer:**
```bash
curl http://localhost:8083/api/messages
```

## API Endpoints

### Producer (port 8082)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/messages` | Send custom message (text/plain body) |
| GET | `/api/messages/send` | Send a sample message |

### Consumer (port 8083)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/messages` | Get all received messages |
| DELETE | `/api/messages` | Clear stored messages |
