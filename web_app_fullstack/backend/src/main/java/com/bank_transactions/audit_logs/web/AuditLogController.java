package com.bank_transactions.audit_logs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.audit_logs.dtos.AuditLogRequestDto;
import com.bank_transactions.audit_logs.dtos.AuditLogResponseDto;
import com.bank_transactions.audit_logs.services.AuditLogService;

@RestController
@RequestMapping("/auditlogs")
@CrossOrigin("http://localhost:4200")
public class AuditLogController {
    @Autowired private AuditLogService service;

    @PostMapping
    public ResponseEntity<AuditLogResponseDto> create(@RequestBody AuditLogRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<AuditLogResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
