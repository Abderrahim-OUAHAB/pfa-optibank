package com.bank_transactions.accounts.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("accounts")
public class Account {

    @PrimaryKey
    private String accountId;

    private String accountNumber;
    private String type;
    private double balance;
    private String status;
    private LocalDate openDate;
    private String customerId;
}
