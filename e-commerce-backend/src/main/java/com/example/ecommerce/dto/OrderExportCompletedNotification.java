package com.example.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderExportCompletedNotification {
    private String filePath;
    private LocalDate exportDate;
    private String recipientEmail;
    private int orderCount;
}
