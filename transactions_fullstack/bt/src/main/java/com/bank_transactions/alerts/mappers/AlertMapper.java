package com.bank_transactions.alerts.mappers;

import java.util.UUID;

import com.bank_transactions.alerts.dtos.AlertRequestDto;
import com.bank_transactions.alerts.dtos.AlertResponseDto;
import com.bank_transactions.alerts.entities.Alert;

public class AlertMapper {
    public static Alert toEntity(AlertRequestDto dto) {
        Alert e = new Alert();
        e.setAlertId(UUID.randomUUID().toString());
        e.setAccountId(dto.getAccountId());
        e.setType(dto.getType());
        e.setMessage(dto.getMessage());
        e.setSeverity(dto.getSeverity());
        e.setStatus(dto.getStatus());
        return e;
    }
    public static AlertResponseDto toResponse(Alert e) {
        AlertResponseDto dto = new AlertResponseDto();
        dto.setAlertId(e.getAlertId());
        dto.setAccountId(e.getAccountId());
        dto.setType(e.getType());
        dto.setMessage(e.getMessage());
        dto.setSeverity(e.getSeverity());
        dto.setStatus(e.getStatus());
        return dto;
    }
}
