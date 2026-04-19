package com.example.kafkaconsumer.service;

import com.example.kafkaconsumer.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    private final List<Message> receivedMessages = new CopyOnWriteArrayList<>();

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(Message message) {
        logger.info("Received message: id={}, content={}, timestamp={}",
                message.id(), message.content(), message.timestamp());
        receivedMessages.add(message);
    }

    public List<Message> getReceivedMessages() {
        return List.copyOf(receivedMessages);
    }

    public void clearMessages() {
        receivedMessages.clear();
    }
}
