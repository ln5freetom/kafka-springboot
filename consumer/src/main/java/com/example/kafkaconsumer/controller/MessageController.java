package com.example.kafkaconsumer.controller;

import com.example.kafkaconsumer.model.Message;
import com.example.kafkaconsumer.service.KafkaConsumerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final KafkaConsumerService consumerService;

    public MessageController(KafkaConsumerService consumerService) {
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
