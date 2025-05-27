package com.bank_transactions.accounts.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_transactions.accounts.dtos.AccountRequestDto;
import com.bank_transactions.accounts.dtos.AccountResponseDto;
import com.bank_transactions.accounts.entities.Account;
import com.bank_transactions.accounts.mappers.AccountMapper;
import com.bank_transactions.accounts.repositories.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository repo;

    @Override
    public AccountResponseDto create(AccountRequestDto dto) {
        Account saved = repo.save(AccountMapper.toEntity(dto));
        return AccountMapper.toResponse(saved);
    }

    @Override
    public List<AccountResponseDto> getAll() {
        return repo.findAll()
                .stream()
                .map(AccountMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByCustomerId(String customerId) {
        repo.deleteByCustomerId(customerId);
    }

    @Override
    public AccountResponseDto findAccountsByCustomerId(String customerId) {
   
        return AccountMapper.toResponse(repo.findAccountsByCustomerId(customerId));

    }

    @Override
    public void updateBalance(String accountId, Double balance) {
       repo.updateBalance(accountId, balance);
    }
}
