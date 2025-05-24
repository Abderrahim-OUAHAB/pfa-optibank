package com.bank_transactions.accounts.mappers;

import com.bank_transactions.accounts.dtos.AccountRequestDto;
import com.bank_transactions.accounts.dtos.AccountResponseDto;
import com.bank_transactions.accounts.entities.Account;

public class AccountMapper {
    public static Account toEntity(AccountRequestDto dto) {
        Account account = new Account();
        account.setAccountId(dto.getAccountId());
        account.setAccountNumber(dto.getAccountNumber());
        account.setType(dto.getType());
        account.setBalance(dto.getBalance());
        account.setStatus(dto.getStatus());
        account.setOpenDate(dto.getOpenDate());
        account.setCustomerId(dto.getCustomerId());
        return account;
    }

    public static AccountResponseDto toResponse(Account account) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setAccountId(account.getAccountId());
        dto.setAccountNumber(account.getAccountNumber());
        dto.setType(account.getType());
        dto.setBalance(account.getBalance());
        dto.setStatus(account.getStatus());
        dto.setOpenDate(account.getOpenDate());
        dto.setCustomerId(account.getCustomerId());
        return dto;
    }
}
