package com.bank_transactions.loans.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.loans.dtos.LoanRequestDto;
import com.bank_transactions.loans.dtos.LoanResponseDto;
import com.bank_transactions.loans.services.LoanService;

@RestController
@RequestMapping("/loans")
@CrossOrigin("http://localhost:4200")
public class LoanController {
    @Autowired private LoanService service;

    @PostMapping
    public ResponseEntity<LoanResponseDto> create(@RequestBody LoanRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<LoanResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
