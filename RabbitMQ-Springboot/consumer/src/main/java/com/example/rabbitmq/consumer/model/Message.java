package com.example.rabbitmq.consumer.model;

import java.time.Instant;

public record Message(
    String id,
    String content,
    Instant timestamp
) {}
