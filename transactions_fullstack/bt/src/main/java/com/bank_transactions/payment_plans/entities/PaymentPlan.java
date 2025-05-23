package com.bank_transactions.payment_plans.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table("payment_plans")
public class PaymentPlan {

    @PrimaryKey
    private String planId;

    private LocalDate dueDate;
    private BigDecimal amount;
    private String status;
    private String loanId;
}
