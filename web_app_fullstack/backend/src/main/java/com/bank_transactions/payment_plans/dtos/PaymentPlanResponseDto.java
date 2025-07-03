package com.bank_transactions.payment_plans.dtos;

import lombok.Data;
import java.time.*;
import java.math.BigDecimal;
@Data
public class PaymentPlanResponseDto {
    private String planId;
    private LocalDate dueDate;
    private BigDecimal amount;
    private String status;
    private String loanId;
}
