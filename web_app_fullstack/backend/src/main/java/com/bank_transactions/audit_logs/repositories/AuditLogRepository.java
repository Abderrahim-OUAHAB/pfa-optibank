package com.bank_transactions.audit_logs.repositories;

import com.bank_transactions.audit_logs.entities.AuditLog;
import org.springframework.data.cassandra.repository.CassandraRepository;
public interface AuditLogRepository extends CassandraRepository<AuditLog, String> {}
