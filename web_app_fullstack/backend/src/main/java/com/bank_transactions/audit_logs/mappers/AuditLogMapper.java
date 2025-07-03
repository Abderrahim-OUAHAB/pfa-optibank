package com.bank_transactions.audit_logs.mappers;

import com.bank_transactions.audit_logs.entities.AuditLog;
import com.bank_transactions.audit_logs.dtos.AuditLogRequestDto;
import com.bank_transactions.audit_logs.dtos.AuditLogResponseDto;
import java.util.UUID;

public class AuditLogMapper {
    public static AuditLog toEntity(AuditLogRequestDto dto) {
        AuditLog e = new AuditLog();
        e .setLogId(UUID.randomUUID().toString());
        e.setAction(dto.getAction());
        e.setTimestamp(dto.getTimestamp());
        e.setIpAddress(dto.getIpAddress());
        e.setUserEmail(dto.getUserEmail());
        return e;
    }
    public static AuditLogResponseDto toResponse(AuditLog e) {
        AuditLogResponseDto dto = new AuditLogResponseDto();
        dto.setLogId(e.getLogId());
        dto.setAction(e.getAction());
        dto.setTimestamp(e.getTimestamp());
        dto.setIpAddress(e.getIpAddress());
        dto.setUserEmail(e.getUserEmail());
        return dto;
    }
}
