package com.example.springshopapp.jms;

import com.example.springshopapp.config.JmsConfig;
import com.example.springshopapp.dto.AuditEventDTO;
import com.example.springshopapp.service.EmailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationListener.class);

    @Autowired
    private EmailNotificationService emailNotificationService;

    // Регулярное выражение для извлечения цены из деталей изменений
    private static final Pattern PRICE_PATTERN = Pattern.compile("price=([\\d.,]+)");

    @JmsListener(destination = JmsConfig.AUDIT_TOPIC,
            containerFactory = "jmsListenerContainerFactory")
    public void receiveAuditEvent(AuditEventDTO event) {
        try {
            logger.info("Email listener received event: {} {} '{}'",
                    event.getEventType(), event.getEntityType(), event.getEntityName());

            // Оповещение о дорогих продуктах
            if (isProductPriceEvent(event)) {
                checkProductPrice(event);
            }

            // Оповещение о создании/удалении важных сущностей
            if (isImportantEntityEvent(event)) {
                emailNotificationService.sendAuditNotification(
                        event.getEventType(),
                        event.getEntityType(),
                        event.getEntityName(),
                        event.getEntityId(),
                        event.getChangeDetails()
                );
            }

        } catch (Exception e) {
            logger.error("Error processing email notification: {}", e.getMessage(), e);
        }
    }

    private boolean isProductPriceEvent(AuditEventDTO event) {
        return "Product".equals(event.getEntityType()) &&
                ("CREATE".equals(event.getEventType()) || "UPDATE".equals(event.getEventType()));
    }

    private boolean isImportantEntityEvent(AuditEventDTO event) {
        // Оповещать о создании/удалении любых сущностей
        return "CREATE".equals(event.getEventType()) || "DELETE".equals(event.getEventType());
    }

    private void checkProductPrice(AuditEventDTO event) {
        try {
            // Извлекаем новую цену из деталей изменений
            Double price = extractPriceFromDetails(event.getChangeDetails());

            if (price != null && price > 1000.0) { // Порог 1000
                emailNotificationService.sendPriceAlert(
                        event.getEntityName(),
                        price,
                        event.getEntityId(),
                        event.getChangeDetails()
                );
            }
        } catch (Exception e) {
            logger.warn("Could not extract price from event: {}", e.getMessage());
        }
    }

    private Double extractPriceFromDetails(String details) {
        if (details == null) {
            return null;
        }

        // Ищем новую цену в формате "price=1234.56"
        Matcher matcher = PRICE_PATTERN.matcher(details);

        // Сначала ищем "New: ... price=xxx"
        if (details.contains("New:")) {
            int newIndex = details.indexOf("New:");
            if (newIndex != -1) {
                String newPart = details.substring(newIndex);
                matcher = PRICE_PATTERN.matcher(newPart);
            }
        }

        if (matcher.find()) {
            try {
                String priceStr = matcher.group(1).replace(',', '.');
                return Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                logger.warn("Could not parse price from: {}", matcher.group(1));
                return null;
            }
        }

        return null;
    }
}