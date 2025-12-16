package com.example.springshopapp.service;

import com.example.springshopapp.config.EmailConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendPriceAlert(String productName, Double price, Long productId, String changeDetails) {
        if (!emailConfig.isEnabled()) {
            logger.info("Email notifications are disabled");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailConfig.getNotificationEmails());
            message.setSubject(String.format("Price Alert: Product '%s' price is %.2f", productName, price));

            String body = String.format(
                    "ALERT: Product price exceeds threshold!\n\n" +
                            "Product: %s (ID: %d)\n" +
                            "Current Price: %.2f\n" +
                            "Threshold: %.2f\n\n" +
                            "Change Details:\n%s\n\n" +
                            "Please review this change.",
                    productName, productId, price, emailConfig.getPriceThreshold(), changeDetails
            );

            message.setText(body);

            mailSender.send(message);

            logger.info("Email alert sent for product '{}' (ID: {}) with price {}",
                    productName, productId, price);

        } catch (Exception e) {
            logger.error("Failed to send email alert: {}", e.getMessage(), e);
        }
    }

    public void sendAuditNotification(String eventType, String entityType, String entityName,
                                      Long entityId, String changeDetails) {
        if (!emailConfig.isEnabled()) {
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(emailConfig.getNotificationEmails());
            message.setSubject(String.format("Audit Event: %s %s '%s'",
                    eventType, entityType, entityName));

            String body = String.format(
                    "AUDIT EVENT NOTIFICATION\n\n" +
                            "Event Type: %s\n" +
                            "Entity Type: %s\n" +
                            "Entity Name: %s\n" +
                            "Entity ID: %d\n\n" +
                            "Change Details:\n%s\n\n" +
                            "Timestamp: %s",
                    eventType, entityType, entityName, entityId, changeDetails,
                    java.time.LocalDateTime.now()
            );

            message.setText(body);

            mailSender.send(message);

            logger.info("Audit notification email sent for {} {} '{}'",
                    eventType, entityType, entityName);

        } catch (Exception e) {
            logger.error("Failed to send audit email: {}", e.getMessage(), e);
        }
    }
}