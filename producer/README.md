# Spring Boot Kafka Producer

A simple Spring Boot application that produces messages to a local Kafka server.

## Prerequisites

1. Java 17+
2. Maven 3.6+
3. Local Kafka running on `localhost:9092`
4. Topic `demo-topic` created in Kafka

## Starting Kafka locally (using Docker)

If you don't have Kafka running locally, you can start it with Docker:

```bash
docker run -d --name zookeeper -p 2181:2181 \
  confluentinc/cp-zookeeper:latest \
  ZOOKEEPER_CLIENT_PORT=2181

docker run -d --name kafka -p 9092:9092 \
  --link zookeeper:zookeeper \
  -e KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 \
  -e KAFKA_BROKER_ID=1 \
  confluentinc/cp-kafka:latest
```

Create the topic:

```bash
docker exec -it kafka \
  kafka-topics --create --topic demo-topic \
  --bootstrap-server localhost:9092 \
  --partitions 3 --replication-factor 1
```

## Build and Run

```bash
# Build
mvn clean package

# Run
mvn spring-boot:run
```

Or run the built jar:

```bash
java -jar target/kafka-producer-1.0.0-SNAPSHOT.jar
```

The application will start on port 8080.

## API Endpoints

### Send a message
```bash
POST /api/messages
Content-Type: text/plain

Your message content here
```

Example:
```bash
curl -X POST -H "Content-Type: text/plain" \
  http://localhost:8080/api/messages \
  -d "Hello, Kafka!"
```

### Send a sample message
```bash
GET /api/messages/send
```

Example:
```bash
curl http://localhost:8080/api/messages/send
```

## Configuration

Configuration is in `src/main/resources/application.properties`:

```properties
# Kafka bootstrap servers
spring.kafka.bootstrap-servers=localhost:9092

# Target topic
app.kafka.topic=demo-topic

# Producer settings
spring.kafka.producer.acks=all
spring.kafka.producer.retries=3
```

## Project Structure

```
src/main/java/com/example/kafkaproducer/
├── KafkaProducerApplication.java   # Main application entry
├── config/
│   └── KafkaProducerConfig.java    # Kafka configuration
├── controller/
│   └── MessageController.java      # REST API endpoints
├── model/
│   └── Message.java                # Message record
└── service/
    └── KafkaProducerService.java   # Kafka producer service
```
