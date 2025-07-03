package com.bank_transactions.transactions.services;

import java.util.List;
import java.util.UUID;

import com.bank_transactions.transactions.dtos.TransactionRequestDto;
import com.bank_transactions.transactions.dtos.TransactionResponseDto;

public interface TransactionService {
public TransactionResponseDto createTransaction(TransactionRequestDto request)  ;
public TransactionResponseDto getTransactionById(String transactionId) ;
public List<TransactionResponseDto> getTransactionsByUserEmail(String userEmail) ;
public List<TransactionResponseDto> getAll() ;
}