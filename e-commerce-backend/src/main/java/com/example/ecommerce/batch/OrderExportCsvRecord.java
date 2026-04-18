package com.example.ecommerce.batch;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderExportCsvRecord(
    Long orderId,
    String orderNumber,
    String status,
    BigDecimal totalAmount,
    String customerEmail,
    String customerName,
    LocalDateTime orderDate
) {}
