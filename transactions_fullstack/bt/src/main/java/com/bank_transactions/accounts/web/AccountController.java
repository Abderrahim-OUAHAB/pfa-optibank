package com.bank_transactions.accounts.web;

import com.bank_transactions.accounts.dtos.AccountRequestDto;
import com.bank_transactions.accounts.dtos.AccountResponseDto;
import com.bank_transactions.accounts.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@CrossOrigin("http://localhost:4200")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping
    public ResponseEntity<AccountResponseDto> create(@RequestBody AccountRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
