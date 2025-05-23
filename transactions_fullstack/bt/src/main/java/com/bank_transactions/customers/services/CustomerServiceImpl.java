package com.bank_transactions.customers.services;

import com.bank_transactions.customers.dtos.CustomerRequestDto;
import com.bank_transactions.customers.dtos.CustomerResponseDto;
import com.bank_transactions.customers.entities.Customer;
import com.bank_transactions.customers.mappers.CustomerMapper;
import com.bank_transactions.customers.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository repo;

    @Override
    public CustomerResponseDto create(CustomerRequestDto dto) {
        Customer customer = CustomerMapper.toEntity(dto);
        Customer saved = repo.save(customer);
        return CustomerMapper.toResponse(saved);
    }

    @Override
    public List<CustomerResponseDto> getAll() {
        return repo.findAll()
                .stream()
                .map(CustomerMapper::toResponse)
                .collect(Collectors.toList());
    }
}
