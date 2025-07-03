package com.bank_transactions.customers.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerRequestDto {
    private String customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private LocalDate birthDate;
    private boolean kycVerified;
}
