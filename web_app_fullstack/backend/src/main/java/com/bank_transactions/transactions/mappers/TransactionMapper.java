package com.bank_transactions.transactions.mappers;

import com.bank_transactions.transactions.dtos.TransactionRequestDto;
import com.bank_transactions.transactions.dtos.TransactionResponseDto;
import com.bank_transactions.transactions.entities.Transaction;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequestDto dto) {
        Transaction entity = new Transaction();

        entity.setTransactionId(dto.getTransactionId()); // Génère un ID aléatoire
        entity.setTransactionAmount(dto.getTransactionAmount());
        entity.setAccountId(dto.getAccountId());
        entity.setTransactionType(dto.getTransactionType());
        entity.setLocation(dto.getLocation());
        entity.setDeviceId(dto.getDeviceId());
        entity.setIpAddress(dto.getIpAddress());
        entity.setMerchantId(dto.getMerchantId());
        entity.setChannel(dto.getChannel());
        entity.setCustomerAge(dto.getCustomerAge());
        entity.setCustomerOccupation(dto.getCustomerOccupation());
        entity.setTransactionDuration(dto.getTransactionDuration());
        entity.setLoginAttempts(dto.getLoginAttempts());
        entity.setAccountBalance(dto.getAccountBalance());
        entity.setTransactionDate(LocalDateTime.now());
        entity.setPreviousTransactionDate(dto.getPreviousTransactionDate()); // Ou prends-le du DTO si présent
        entity.setUserEmail(dto.getUserEmail());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    public TransactionResponseDto toDto(Transaction entity) {
        TransactionResponseDto dto = new TransactionResponseDto();

        dto.setTransactionId(entity.getTransactionId());
        dto.setTransactionAmount(entity.getTransactionAmount());
        dto.setTransactionType(entity.getTransactionType());
        dto.setAccountId(entity.getAccountId());
        dto.setLocation(entity.getLocation());
        dto.setDeviceId(entity.getDeviceId());
        dto.setIpAddress(entity.getIpAddress());
        dto.setMerchantId(entity.getMerchantId());
        dto.setChannel(entity.getChannel());
        dto.setCustomerAge(entity.getCustomerAge());
        dto.setCustomerOccupation(entity.getCustomerOccupation());
        dto.setTransactionDuration(entity.getTransactionDuration());
        dto.setLoginAttempts(entity.getLoginAttempts());
        dto.setAccountBalance(entity.getAccountBalance());
        dto.setTransactionDate(entity.getTransactionDate());
        dto.setPreviousTransactionDate(entity.getPreviousTransactionDate());
        dto.setUserEmail(entity.getUserEmail());
        dto.setStatus(entity.getStatus());
        return dto;
    }
}
