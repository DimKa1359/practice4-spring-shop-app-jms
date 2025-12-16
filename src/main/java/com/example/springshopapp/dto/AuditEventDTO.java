package com.example.springshopapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AuditEventDTO implements Serializable {

    private String eventType;      // CREATE, UPDATE, DELETE
    private String entityType;     // Product, Category
    private Long entityId;
    private String entityName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime timestamp;

    private String changeDetails;  // JSON или текст с деталями изменений
    private String userInfo;
    private String ipAddress;

    // Конструкторы
    public AuditEventDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public AuditEventDTO(String eventType, String entityType, Long entityId, String entityName) {
        this();
        this.eventType = eventType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.entityName = entityName;
    }

    // Геттеры и сеттеры
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

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getChangeDetails() {
        return changeDetails;
    }

    public void setChangeDetails(String changeDetails) {
        this.changeDetails = changeDetails;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    @Override
    public String toString() {
        return "AuditEventDTO{" +
                "eventType='" + eventType + '\'' +
                ", entityType='" + entityType + '\'' +
                ", entityId=" + entityId +
                ", entityName='" + entityName + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}