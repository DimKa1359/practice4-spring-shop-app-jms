package com.example.springshopapp.service;

import com.example.springshopapp.dto.AuditEventDTO;
import com.example.springshopapp.entity.AuditLog;
import com.example.springshopapp.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Transactional
    public void saveAuditLog(AuditEventDTO event) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setEventType(event.getEventType());
            auditLog.setEntityType(event.getEntityType());
            auditLog.setEntityId(event.getEntityId());
            auditLog.setChangeDetails(event.getChangeDetails());
            auditLog.setUserInfo(event.getUserInfo());
            auditLog.setIpAddress(event.getIpAddress());

            auditLogRepository.save(auditLog);

            System.out.println("Аудит записан в БД: " + event.getEventType() +
                    " " + event.getEntityType() + " ID=" + event.getEntityId());
        } catch (Exception e) {
            System.err.println("Ошибка записи аудита в БД: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Метод для тестирования
    public long getAuditLogCount() {
        return auditLogRepository.count();
    }
}