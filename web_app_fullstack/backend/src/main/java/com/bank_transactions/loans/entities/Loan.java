package com.bank_transactions.loans.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table("loans")
public class Loan {

    @PrimaryKey
    private String loanId;

    private BigDecimal amount;
    private double interestRate;
    private int term;
    private LocalDate startDate;
    private String status;
    private String accountId;
}
