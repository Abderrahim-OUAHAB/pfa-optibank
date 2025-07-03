package com.bank_transactions.transactions.web;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.transactions.dtos.TransactionRequestDto;
import com.bank_transactions.transactions.dtos.TransactionResponseDto;
import com.bank_transactions.transactions.services.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:4200")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{transactionId}")
    public TransactionResponseDto getTransaction(@PathVariable String transactionId) {
        return transactionService.getTransactionById(transactionId);
    }

    @PostMapping("/createTransaction")
    public TransactionResponseDto createTransaction(
            @RequestBody TransactionRequestDto request) { 

      
        return transactionService.createTransaction(request);
    }

    @GetMapping("/user/{userEmail}")
    public List<TransactionResponseDto> getTransactionsByUserEmail(@PathVariable String userEmail) {
        return transactionService.getTransactionsByUserEmail(userEmail);
    }

    @GetMapping("/")
    public List<TransactionResponseDto> getAll() {
        return transactionService.getAll();
    }
    
}