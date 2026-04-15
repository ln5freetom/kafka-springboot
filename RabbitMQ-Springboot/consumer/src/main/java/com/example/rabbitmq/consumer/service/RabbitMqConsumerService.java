package com.example.rabbitmq.consumer.service;

import com.example.rabbitmq.consumer.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RabbitMqConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqConsumerService.class);

    private final List<Message> receivedMessages = new ArrayList<>();

    @RabbitListener(queues = "${app.rabbitmq.queue}")
    public void consume(Message message) {
        logger.info("Received message: id={}, content={}, timestamp={}",
                message.id(), message.content(), message.timestamp());
        receivedMessages.add(message);
    }

    public List<Message> getReceivedMessages() {
        return new ArrayList<>(receivedMessages);
    }

    public void clearMessages() {
        receivedMessages.clear();
    }
}
