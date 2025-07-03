package com.bank_transactions.accounts.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AccountRequestDto {
    private String accountId;
    private String accountNumber;
    private String type;
    private double balance;
    private String status;
    private LocalDate openDate;
    private String customerId;
}
