package com.bank_transactions.audit_logs.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("audit_logs")
public class AuditLog {

    @PrimaryKey
    private String logId;

    private String action;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String userEmail;
}
