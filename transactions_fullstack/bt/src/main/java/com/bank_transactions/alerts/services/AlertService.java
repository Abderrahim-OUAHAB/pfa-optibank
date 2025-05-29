package com.bank_transactions.alerts.services;

import com.bank_transactions.alerts.dtos.AlertRequestDto;
import com.bank_transactions.alerts.dtos.AlertResponseDto;
import java.util.List;

public interface AlertService {
    AlertResponseDto create(AlertRequestDto dto);
    List<AlertResponseDto> getAll();
    void updateAlertStatus(String alertId, String status);
    List<AlertResponseDto> findAlertsByAccountId(String accountId);
    void deleteByAccountId(String accountId);
}
