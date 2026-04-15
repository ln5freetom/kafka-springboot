package com.example.rabbitmq.consumer.controller;

import com.example.rabbitmq.consumer.model.Message;
import com.example.rabbitmq.consumer.service.RabbitMqConsumerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final RabbitMqConsumerService consumerService;

    public MessageController(RabbitMqConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping
    public ResponseEntity<List<Message>> getReceivedMessages() {
        return ResponseEntity.ok(consumerService.getReceivedMessages());
    }

    @DeleteMapping
    public ResponseEntity<Void> clearMessages() {
        consumerService.clearMessages();
        return ResponseEntity.noContent().build();
    }
}
