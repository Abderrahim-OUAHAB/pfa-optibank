package com.bank_transactions.customers.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_transactions.customers.dtos.CustomerRequestDto;
import com.bank_transactions.customers.dtos.CustomerResponseDto;
import com.bank_transactions.customers.entities.Customer;
import com.bank_transactions.customers.mappers.CustomerMapper;
import com.bank_transactions.customers.repositories.CustomerRepository;

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

    @Override
    public void deleteByCustomerId(String email) {
        repo.deleteByCustomerId(email);
    }

    @Override
    public CustomerResponseDto findByCustomerId(String email) {
       return CustomerMapper.toResponse(repo.findByCustomerId(email));
    }
    @Override
        public void updateCustomer(String email, CustomerRequestDto dto) {
        Customer customer = repo.findById(email).orElseThrow();
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setBirthDate(dto.getBirthDate());
        customer.setPhone(dto.getPhone());
        customer.setAddress(dto.getAddress());
        repo.save(customer);
    }
}
