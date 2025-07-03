package com.bank_transactions.loans.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_transactions.loans.dtos.LoanRequestDto;
import com.bank_transactions.loans.dtos.LoanResponseDto;
import com.bank_transactions.loans.mappers.LoanMapper;
import com.bank_transactions.loans.repositories.LoanRepository;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired private LoanRepository repo;

    public LoanResponseDto create(LoanRequestDto dto) {
        return LoanMapper.toResponse(repo.save(LoanMapper.toEntity(dto)));
    }
    public List<LoanResponseDto> getAll() {
        return repo.findAll().stream().map(LoanMapper::toResponse).collect(Collectors.toList());
    }
}
