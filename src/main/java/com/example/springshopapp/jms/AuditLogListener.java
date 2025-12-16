package com.example.springshopapp.jms;

import com.example.springshopapp.config.JmsConfig;
import com.example.springshopapp.dto.AuditEventDTO;
import com.example.springshopapp.service.AuditLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class AuditLogListener {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogListener.class);

    @Autowired
    private AuditLogService auditLogService;

    @JmsListener(destination = JmsConfig.AUDIT_TOPIC,
            containerFactory = "jmsListenerContainerFactory")
    public void receiveAuditEvent(AuditEventDTO event) {
        try {
            logger.info("=== JMS СООБЩЕНИЕ ПОЛУЧЕНО ===");
            logger.info("Тип события: {}", event.getEventType());
            logger.info("Тип сущности: {}", event.getEntityType());
            logger.info("ID сущности: {}", event.getEntityId());
            logger.info("Имя сущности: {}", event.getEntityName());
            logger.info("Время: {}", event.getTimestamp());
            logger.info("Детали: {}", event.getChangeDetails());
            logger.info("IP: {}", event.getIpAddress());
            logger.info("Пользователь: {}", event.getUserInfo());
            logger.info("=============================");

            // Проверяем структуру сообщения
            if (isValidEvent(event)) {
                auditLogService.saveAuditLog(event);
                logger.info("Аудит успешно сохранен в БД");
            } else {
                logger.warn("Некорректное сообщение аудита: {}", event);
            }

        } catch (Exception e) {
            logger.error("Ошибка обработки аудит-события: {}", e.getMessage(), e);
        }
    }

    private boolean isValidEvent(AuditEventDTO event) {
        return event != null &&
                event.getEventType() != null && !event.getEventType().isEmpty() &&
                event.getEntityType() != null && !event.getEntityType().isEmpty() &&
                event.getEntityId() != null &&
                (event.getEventType().equals("CREATE") ||
                        event.getEventType().equals("UPDATE") ||
                        event.getEventType().equals("DELETE"));
    }
}