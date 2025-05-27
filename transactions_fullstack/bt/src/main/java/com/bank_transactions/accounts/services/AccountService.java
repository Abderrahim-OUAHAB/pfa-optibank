package com.bank_transactions.accounts.services;

import com.bank_transactions.accounts.dtos.AccountRequestDto;
import com.bank_transactions.accounts.dtos.AccountResponseDto;

import java.util.List;

public interface AccountService {
    AccountResponseDto create(AccountRequestDto dto);
    List<AccountResponseDto> getAll();
    void deleteByCustomerId(String customerId);
    AccountResponseDto findAccountsByCustomerId(String customerId);
    void updateBalance( String accountId, Double balance);
}
