package com.bank_transactions.transactions.entities;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.*;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Table("transactions")
public class Transaction {

    @PrimaryKey
    @Column("transaction_id")
    private String transactionId;

    @Column("account_id")
    private String accountId;

    @Column("user_email")
    private String userEmail;

    @Column("transaction_amount")
    private BigDecimal transactionAmount;

    @Column("transaction_type")
    private String transactionType; 

    private String location;
    
    @Column("device_id")
    private String deviceId;
    
    @Column("ip_address")
    private String ipAddress;
    
    @Column("merchant_id")
    private String merchantId;
    
    private String channel;
    
    @Column("customer_age")
    private Integer customerAge;
    
    @Column("customer_occupation")
    private String customerOccupation;
    
    @Column("transaction_duration")
    private Integer transactionDuration;
    
    @Column("login_attempts")
    private Integer loginAttempts;
    
    @Column("account_balance")
    private BigDecimal accountBalance;
   
    @Column("transaction_date")
    private LocalDateTime transactionDate;
    @Column("previous_transaction_date")
    private LocalDateTime previousTransactionDate;

    @Column("status")
    @Enumerated(EnumType.STRING)
    private Status status=Status.PENDING;

    public enum Status {
    PENDING,       
    APPROVED,
    REJECTED      

}
  

}