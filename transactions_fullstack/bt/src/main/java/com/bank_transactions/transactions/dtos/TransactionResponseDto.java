package com.bank_transactions.transactions.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponseDto {
    private UUID transactionId;
    private String accountId;
    private String userEmail;
    private LocalDateTime transactionDate;
    private BigDecimal transactionAmount;
    private String transactionType;
    private String location;
    private String deviceId;
    private String ipAddress;
    private String merchantId;
    private String channel;
    private Integer customerAge;
    private String customerOccupation;
    private Integer transactionDuration;
    private Integer loginAttempts;
    private BigDecimal accountBalance;
    private LocalDateTime previousTransactionDate;
}