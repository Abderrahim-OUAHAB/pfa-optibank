package com.bank_transactions.auth.dtos;

import java.time.LocalDate;

import com.bank_transactions.auth.entities.User.Role;
import com.bank_transactions.auth.entities.User.Status;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor 
@AllArgsConstructor
public class UserDto {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private String cin;
    private String nationality;
    private String profession;
    private double monthlyIncome;
    private LocalDate birthDate;
    private Status status;
    private Role role;
}
