package com.bank_transactions.alerts.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_transactions.alerts.dtos.AlertRequestDto;
import com.bank_transactions.alerts.dtos.AlertResponseDto;
import com.bank_transactions.alerts.mappers.AlertMapper;
import com.bank_transactions.alerts.repositories.AlertRepository;

@Service
public class AlertServiceImpl implements AlertService {
    @Autowired private AlertRepository repo;

    public AlertResponseDto create(AlertRequestDto dto) {
        return AlertMapper.toResponse(repo.save(AlertMapper.toEntity(dto)));
    }
    public List<AlertResponseDto> getAll() {
        return repo.findAll().stream().map(AlertMapper::toResponse).collect(Collectors.toList());
    }
    @Override
    public void updateAlertStatus(String alertId, String status) {
        repo.updateAlertStatus(alertId, status);
    }
    @Override
    public List<AlertResponseDto> findAlertsByAccountId(String accountId) {
        return repo.findAlertsByAccountId(accountId).stream().map(AlertMapper::toResponse).collect(Collectors.toList());
    }
    @Override
    public void deleteByAccountId(String accountId) {
        repo.deleteByAccountId(accountId);
    }
}
