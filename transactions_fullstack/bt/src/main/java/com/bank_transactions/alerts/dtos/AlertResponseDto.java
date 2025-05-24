package com.bank_transactions.alerts.dtos;

import lombok.Data;
@Data
public class AlertResponseDto {
    private String alertId;
        private String accountId;
    private String type;
    private String message;
    private String severity;
    private String status;
}
