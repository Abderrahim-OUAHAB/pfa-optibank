package com.bank_transactions.transactions.dtos;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.bank_transactions.transactions.entities.Transaction.Status;

@Data
public class TransactionRequestDto {
    private String transactionId;
    private String accountId;
    private String userEmail;


    private BigDecimal transactionAmount;
    
 
    private String transactionType; // "DEBIT", "CREDIT", "TRANSFER"
    
    private String location;
    private String deviceId;
    private String ipAddress;
    private String merchantId;
    private String channel;
      private LocalDateTime previousTransactionDate;
 

    private Integer customerAge;
    
    private String customerOccupation;
    private Integer transactionDuration;
    private Integer loginAttempts;
    private BigDecimal accountBalance;
     private Status status=Status.PENDING;
}