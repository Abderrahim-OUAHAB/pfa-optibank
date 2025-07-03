package com.bank_transactions.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor 
@AllArgsConstructor
public class AuthRequestDto {
    private String email;
    private String password;
}
