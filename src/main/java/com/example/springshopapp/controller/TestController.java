package com.example.springshopapp.controller;

import com.example.springshopapp.service.EmailNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private EmailNotificationService emailNotificationService;

    @GetMapping("/test-email-high-price")
    public String testEmailHighPrice() {
        try {
            // Тестовые данные для дорогого товара
            emailNotificationService.sendPriceAlert(
                    "Test Expensive Laptop",
                    1500.0,
                    999L,
                    "Product created: name='Test Expensive Laptop', price=1500.00, category='Electronics'"
            );
            return "Email notification sent for high price product!";
        } catch (Exception e) {
            return "Error sending email: " + e.getMessage();
        }
    }

    @GetMapping("/test-email-audit")
    public String testEmailAudit() {
        try {
            // Тестовые данные для аудита
            emailNotificationService.sendAuditNotification(
                    "CREATE",
                    "Product",
                    "Test Product",
                    100L,
                    "Test change details"
            );
            return "Email audit notification sent!";
        } catch (Exception e) {
            return "Error sending audit email: " + e.getMessage();
        }
    }

    @GetMapping("/test-jms")
    public String testJms() {
        try {
            // Можно добавить тест JMS если нужно
            return "JMS test endpoint - add implementation";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}