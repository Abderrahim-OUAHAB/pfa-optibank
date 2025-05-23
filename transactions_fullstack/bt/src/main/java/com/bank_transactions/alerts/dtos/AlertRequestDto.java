package com.bank_transactions.alerts.dtos;

import lombok.Data;
@Data
public class AlertRequestDto {
    private String type;
    private String message;
    private String severity;
    private String status;
}
