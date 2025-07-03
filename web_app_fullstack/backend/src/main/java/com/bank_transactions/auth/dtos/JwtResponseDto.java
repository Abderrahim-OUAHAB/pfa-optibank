package com.bank_transactions.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor 
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
    private String role;
    private String email;
}
