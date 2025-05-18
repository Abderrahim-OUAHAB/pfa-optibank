package com.bank_transactions.auth.entities;

import java.time.LocalDate;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;


@lombok.Data
@Table("users")
public class User {

    @PrimaryKey
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
    @Enumerated(EnumType.STRING)
    private Status status=Status.PENDING;
    @Enumerated(EnumType.STRING)
    private Role role=Role.USER;

    

public enum Status {
    PENDING,       
    APPROVED,
    REJECTED,      
    SUSPENDED      


}

public enum Role {
    USER,
    ADMIN
}

}