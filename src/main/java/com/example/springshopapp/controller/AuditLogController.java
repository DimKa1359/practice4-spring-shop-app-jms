package com.example.springshopapp.controller;

import com.example.springshopapp.entity.AuditLog;
import com.example.springshopapp.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/audit")
public class AuditLogController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping
    public String listAuditLogs(Model model) {
        try {
            List<AuditLog> auditLogs = auditLogRepository.findAll();
            model.addAttribute("auditLogs", auditLogs);
            return "audit_logs";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error loading audit logs: " + e.getMessage());
            return "audit_logs";
        }
    }
}