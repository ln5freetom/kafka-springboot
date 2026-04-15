package com.example.rabbitmq.producer.service;

import com.example.rabbitmq.producer.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqProducerService {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMqProducerService.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    public RabbitMqProducerService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Message message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        logger.info("Sent message=[{}] to exchange=[{}] with routingKey=[{}]",
                message.content(), exchange, routingKey);
    }
}
