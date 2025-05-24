package com.bank_transactions.customers.mappers;

import com.bank_transactions.customers.dtos.CustomerRequestDto;
import com.bank_transactions.customers.dtos.CustomerResponseDto;
import com.bank_transactions.customers.entities.Customer;

import java.util.UUID;

public class CustomerMapper {

    public static Customer toEntity(CustomerRequestDto dto) {
        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        customer.setBirthDate(dto.getBirthDate());
        customer.setKycVerified(dto.isKycVerified());
        return customer;
    }

    public static CustomerResponseDto toResponse(Customer entity) {
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setCustomerId(entity.getCustomerId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setAddress(entity.getAddress());
        dto.setBirthDate(entity.getBirthDate());
        dto.setKycVerified(entity.isKycVerified());
        return dto;
    }
}
