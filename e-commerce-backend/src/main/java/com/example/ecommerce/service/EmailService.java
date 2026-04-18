package com.example.ecommerce.service;

import com.example.ecommerce.dto.OrderExportCompletedNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @KafkaListener(topics = "${app.kafka.email-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleExportNotification(OrderExportCompletedNotification notification) {
        logger.info("Received order export completion notification: {} orders exported to {}",
                notification.getOrderCount(), notification.getFilePath());

        try {
            sendExportEmail(notification);
            logger.info("Export completion email sent successfully to {}", notification.getRecipientEmail());
        } catch (Exception e) {
            logger.error("Failed to send export completion email: {}", e.getMessage(), e);
        }
    }

    private void sendExportEmail(OrderExportCompletedNotification notification) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(notification.getRecipientEmail());
        helper.setFrom("no-reply@ecommerce.com");
        helper.setSubject("Daily Order Export - " + notification.getExportDate());

        String emailBody = String.format("""
                <h1>Daily Order Export Report</h1>
                <p>Date: %s</p>
                <p>Total orders exported: %d</p>
                <p>Please find the exported CSV file attached.</p>
                """, notification.getExportDate(), notification.getOrderCount());

        helper.setText(emailBody, true);

        File exportFile = new File(notification.getFilePath());
        if (exportFile.exists()) {
            helper.addAttachment(exportFile.getName(), exportFile);
        }

        try {
            javaMailSender.send(mimeMessage);
        } catch (MailException e) {
            throw new MessagingException("Failed to send email", e);
        }
    }
}
