package com.example.kafkaproducer.service;

import com.example.kafkaproducer.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, Message> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;

    public KafkaProducerService(KafkaTemplate<String, Message> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public CompletableFuture<SendResult<String, Message>> sendMessage(Message message) {
        CompletableFuture<SendResult<String, Message>> future = kafkaTemplate.send(topic, message.id(), message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Sent message=[{}] with offset=[{}] to topic=[{}]",
                        message.content(), result.getRecordMetadata().offset(), topic);
            } else {
                logger.error("Unable to send message=[{}] due to: {}", message.content(), ex.getMessage(), ex);
            }
        });
        return future;
    }
}
