package com.example.springshopapp.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_timestamp", nullable = false)
    private LocalDateTime eventTimestamp;

    @Column(name = "event_type", nullable = false, length = 20)
    private String eventType; // CREATE, UPDATE, DELETE

    @Column(name = "entity_type", nullable = false, length = 100)
    private String entityType; // Product, Category

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "user_info", length = 255)
    private String userInfo;

    @Column(name = "change_details", columnDefinition = "TEXT")
    private String changeDetails;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    // Конструкторы
    public AuditLog() {
        this.eventTimestamp = LocalDateTime.now();
    }

    public AuditLog(String eventType, String entityType, Long entityId) {
        this();
        this.eventType = eventType;
        this.entityType = entityType;
        this.entityId = entityId;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(LocalDateTime eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getChangeDetails() {
        return changeDetails;
    }

    public void setChangeDetails(String changeDetails) {
        this.changeDetails = changeDetails;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "id=" + id +
                ", eventTimestamp=" + eventTimestamp +
                ", eventType='" + eventType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                '}';
    }
}