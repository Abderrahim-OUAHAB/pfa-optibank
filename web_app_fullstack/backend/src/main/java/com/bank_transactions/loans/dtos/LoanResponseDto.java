package com.bank_transactions.loans.dtos;

import lombok.Data;
import java.time.*;
import java.math.BigDecimal;
@Data
public class LoanResponseDto {
    private String loanId;
    private BigDecimal amount;
    private double interestRate;
    private int term;
    private LocalDate startDate;
    private String status;
    private String accountId;
}
