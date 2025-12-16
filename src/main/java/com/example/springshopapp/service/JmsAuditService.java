package com.example.springshopapp.service;

import com.example.springshopapp.config.JmsConfig;
import com.example.springshopapp.dto.AuditEventDTO;
import com.example.springshopapp.util.RequestContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsAuditService {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendAuditEvent(AuditEventDTO event) {
        try {
            // Добавляем контекстную информацию
            event.setIpAddress(RequestContextUtil.getClientIp());
            event.setUserInfo(RequestContextUtil.getUserInfo());

            jmsTemplate.convertAndSend(JmsConfig.AUDIT_TOPIC, event);
            System.out.println("JMS сообщение отправлено: " + event);
        } catch (Exception e) {
            System.err.println("Ошибка отправки JMS сообщения: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Вспомогательные методы для создания событий
    public void sendProductEvent(String eventType, Long productId, String productName,
                                 String changeDetails) {
        AuditEventDTO event = new AuditEventDTO(
                eventType, "Product", productId, productName
        );
        event.setChangeDetails(changeDetails);
        sendAuditEvent(event);
    }

    public void sendCategoryEvent(String eventType, Long categoryId, String categoryName,
                                  String changeDetails) {
        AuditEventDTO event = new AuditEventDTO(
                eventType, "Category", categoryId, categoryName
        );
        event.setChangeDetails(changeDetails);
        sendAuditEvent(event);
    }
}