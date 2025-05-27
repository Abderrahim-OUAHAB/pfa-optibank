package com.bank_transactions.customers.services;

import com.bank_transactions.customers.dtos.CustomerRequestDto;
import com.bank_transactions.customers.dtos.CustomerResponseDto;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto create(CustomerRequestDto dto);
    List<CustomerResponseDto> getAll();
    void deleteByCustomerId(String email);
    CustomerResponseDto findByCustomerId(String email);
    void updateCustomer(String email, CustomerRequestDto dto);
}
