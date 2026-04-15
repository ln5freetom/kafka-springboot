package com.example.rabbitmq.producer.model;

import java.time.Instant;

public record Message(
    String id,
    String content,
    Instant timestamp
) {}
