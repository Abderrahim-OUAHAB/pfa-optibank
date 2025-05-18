package com.bank_transactions.transactions.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransactionRequestDto {
    private String accountId;
    private String userEmail;


    private BigDecimal transactionAmount;
    
 
    private String transactionType; // "DEBIT", "CREDIT", "TRANSFER"
    
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
}