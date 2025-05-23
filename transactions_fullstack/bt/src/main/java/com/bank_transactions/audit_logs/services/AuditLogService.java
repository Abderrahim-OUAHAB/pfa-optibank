package com.bank_transactions.audit_logs.services;

import com.bank_transactions.audit_logs.dtos.AuditLogRequestDto;
import com.bank_transactions.audit_logs.dtos.AuditLogResponseDto;
import java.util.List;

public interface AuditLogService {
    AuditLogResponseDto create(AuditLogRequestDto dto);
    List<AuditLogResponseDto> getAll();
}
