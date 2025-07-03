package com.bank_transactions.audit_logs.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_transactions.audit_logs.dtos.AuditLogRequestDto;
import com.bank_transactions.audit_logs.dtos.AuditLogResponseDto;
import com.bank_transactions.audit_logs.mappers.AuditLogMapper;
import com.bank_transactions.audit_logs.repositories.AuditLogRepository;

@Service
public class AuditLogServiceImpl implements AuditLogService {
    @Autowired private AuditLogRepository repo;

    public AuditLogResponseDto create(AuditLogRequestDto dto) {
        return AuditLogMapper.toResponse(repo.save(AuditLogMapper.toEntity(dto)));
    }
    public List<AuditLogResponseDto> getAll() {
        return repo.findAll().stream().map(AuditLogMapper::toResponse).collect(Collectors.toList());
    }
}
