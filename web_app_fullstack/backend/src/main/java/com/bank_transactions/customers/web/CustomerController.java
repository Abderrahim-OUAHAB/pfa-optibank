package com.bank_transactions.customers.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.customers.dtos.CustomerRequestDto;
import com.bank_transactions.customers.dtos.CustomerResponseDto;
import com.bank_transactions.customers.services.CustomerService;

@RestController
@RequestMapping("/customers")
@CrossOrigin("http://localhost:4200")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDto> create(@RequestBody CustomerRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/")
    public ResponseEntity<List<CustomerResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String email) {
        service.deleteByCustomerId(email);
        return ResponseEntity.ok(Map.of("message", "Customer deleted"));
    }

    @GetMapping("/find/{email}")
    public ResponseEntity<CustomerResponseDto> findAccountsByCustomerId(@PathVariable String email) {
        return ResponseEntity.ok(service.findByCustomerId(email));
    }
    @PutMapping("/update/{email}")
    public ResponseEntity<Map<String, String>> update(@PathVariable String email, @RequestBody CustomerRequestDto dto) {
        service.updateCustomer(email, dto);
        return ResponseEntity.ok(Map.of("message", "Customer updated"));
    }
}
