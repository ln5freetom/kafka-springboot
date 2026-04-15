# Spring Boot Kafka Demo

A complete demo with Spring Boot Kafka **Producer** and **Consumer** applications.

## Projects

- [`producer/`](producer/) - Kafka Producer application that sends JSON messages via REST API
  - Runs on port `8080`
  - Sends messages to `demo-topic`

- [`consumer/`](consumer/) - Kafka Consumer application that consumes messages and exposes them via REST API
  - Runs on port `8081`
  - Stores received messages in-memory for inspection

## Architecture

- Both apps use **Spring Boot 3.2 + Spring Kafka**
- Messages are serialized/deserialized as JSON
- All configuration is externalized in `application.properties`
- Producer uses async callback with logging
- Consumer uses `@KafkaListener` annotation for message consumption

## Quick Start

### 1. Start Kafka locally (with Docker)

```bash
# Start Zookeeper
docker run -d --name zookeeper -p 2181:2181 confluentinc/cp-zookeeper:latest

# Start Kafka
docker run -d --name kafka -p 9092:9092 \
  --link zookeeper:zookeeper \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 \
  -e KAFKA_BROKER_ID=1 \
  confluentinc/cp-kafka:latest

# Create topic
docker exec -it kafka \
  kafka-topics --create --topic demo-topic \
  --bootstrap-server localhost:9092 \
  --partitions 3 --replication-factor 1
```

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
curl http://localhost:8080/api/messages/send
```

**Check received messages in consumer:**
```bash
curl http://localhost:8081/api/messages
```

Example response:
```json
[
  {
    "id": "uuid-123",
    "content": "Hello Kafka from Spring Boot!",
    "timestamp": "2024-04-15T10:00:00Z"
  }
]
```

## API Endpoints

### Producer (port 8080)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/messages` | Send custom message (text/plain body) |
| GET | `/api/messages/send` | Send a sample message |

### Consumer (port 8081)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/messages` | Get all received messages |
| DELETE | `/api/messages` | Clear stored messages |

## Configuration

Both applications read all Kafka settings from `application.properties`:

**Producer:** `producer/src/main/resources/application.properties`
```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3
app.kafka.topic=demo-topic
```

**Consumer:** `consumer/src/main/resources/application.properties`
```properties
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=demo-group
spring.kafka.consumer.auto-offset-reset=earliest
app.kafka.topic=demo-topic
```

## Requirements

- Java 17+
- Maven 3.6+
- Kafka 2.8+ (local or remote)
