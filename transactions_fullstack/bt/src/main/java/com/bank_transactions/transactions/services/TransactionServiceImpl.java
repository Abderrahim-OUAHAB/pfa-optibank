package com.bank_transactions.transactions.services;

import com.bank_transactions.transactions.dtos.*;
import com.bank_transactions.transactions.entities.*;
import com.bank_transactions.transactions.exceptions.*;
import com.bank_transactions.transactions.mappers.*;
import com.bank_transactions.transactions.repositories.*;
import com.bank_transactions.transactions.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionResponseDto createTransaction(TransactionRequestDto request) {
        validateTransaction(request);
        
        Transaction transaction = transactionMapper.toEntity(request);
        
        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }

    private void validateTransaction(TransactionRequestDto request) {
        if (request.getTransactionAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Le montant doit être positif");
        }
    }

    @Override
    public TransactionResponseDto getTransactionById(String transactionId) {
        return transactionMapper.toDto(
            transactionRepository.findByTransactionId(transactionId)
        );
    }

 
}