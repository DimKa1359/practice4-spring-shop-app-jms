package com.example.springshopapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Value("${app.notification.emails:admin@shop.com}")
    private String notificationEmails;

    @Value("${app.notification.price-threshold:1000.0}")
    private double priceThreshold;

    @Value("${app.notification.enabled:true}")
    private boolean enabled;

    public String[] getNotificationEmails() {
        return notificationEmails.split(",");
    }

    public double getPriceThreshold() {
        return priceThreshold;
    }

    public boolean isEnabled() {
        return enabled;
    }
}