package com.bank_transactions.loans.services;

import com.bank_transactions.loans.dtos.LoanRequestDto;
import com.bank_transactions.loans.dtos.LoanResponseDto;
import java.util.List;

public interface LoanService {
    LoanResponseDto create(LoanRequestDto dto);
    List<LoanResponseDto> getAll();
}
