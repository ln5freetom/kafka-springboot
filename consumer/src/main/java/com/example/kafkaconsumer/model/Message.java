package com.example.kafkaconsumer.model;

import java.time.Instant;

public record Message(
    String id,
    String content,
    Instant timestamp
) {}
