package com.example.ecommerce.batch;

import com.example.ecommerce.entity.Order;
import org.springframework.batch.item.ItemProcessor;

public class OrderExportItemProcessor implements ItemProcessor<Order, OrderExportCsvRecord> {

    @Override
    public OrderExportCsvRecord process(Order order) {
        String customerName = order.getUser().getFirstName() + " " + order.getUser().getLastName();
        return new OrderExportCsvRecord(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getUser().getEmail(),
                customerName.trim(),
                order.getCreatedAt()
        );
    }
}
