package com.bank_transactions.customers.web;

import com.bank_transactions.customers.dtos.CustomerRequestDto;
import com.bank_transactions.customers.dtos.CustomerResponseDto;
import com.bank_transactions.customers.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@CrossOrigin("http://localhost:4200")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerResponseDto> create(@RequestBody CustomerRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
