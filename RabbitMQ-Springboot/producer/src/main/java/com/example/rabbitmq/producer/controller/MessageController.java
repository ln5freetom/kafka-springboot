package com.example.rabbitmq.producer.controller;

import com.example.rabbitmq.producer.model.Message;
import com.example.rabbitmq.producer.service.RabbitMqProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final RabbitMqProducerService producerService;

    public MessageController(RabbitMqProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody String content) {
        Message message = new Message(
                UUID.randomUUID().toString(),
                content,
                Instant.now()
        );
        producerService.sendMessage(message);
        return ResponseEntity.ok("Message sent successfully: " + message.id());
    }

    @GetMapping("/send")
    public ResponseEntity<String> sendSampleMessage() {
        Message message = new Message(
                UUID.randomUUID().toString(),
                "Hello RabbitMQ from Spring Boot! " + Instant.now(),
                Instant.now()
        );
        producerService.sendMessage(message);
        return ResponseEntity.ok("Sample message sent: " + message.content());
    }
}
