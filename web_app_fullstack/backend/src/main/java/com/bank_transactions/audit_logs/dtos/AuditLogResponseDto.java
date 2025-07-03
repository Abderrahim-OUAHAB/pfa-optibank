package com.bank_transactions.audit_logs.dtos;

import lombok.Data;
import java.time.*;
import java.math.BigDecimal;
@Data
public class AuditLogResponseDto {
    private String logId;
    private String action;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String userEmail;
}
