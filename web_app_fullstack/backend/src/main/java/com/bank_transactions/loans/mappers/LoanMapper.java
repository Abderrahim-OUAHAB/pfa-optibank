package com.bank_transactions.loans.mappers;

import com.bank_transactions.loans.entities.Loan;
import com.bank_transactions.loans.dtos.LoanRequestDto;
import com.bank_transactions.loans.dtos.LoanResponseDto;
import java.util.UUID;

public class LoanMapper {
    public static Loan toEntity(LoanRequestDto dto) {
        Loan e = new Loan();
        e.setLoanId(UUID.randomUUID().toString());
        e.setAccountId(dto.getAccountId());
        e.setAmount(dto.getAmount());
        e.setInterestRate(dto.getInterestRate());
        e.setTerm(dto.getTerm());
        e.setStartDate(dto.getStartDate());
        e.setStatus(dto.getStatus());
        return e;
    }
    public static LoanResponseDto toResponse(Loan e) {
        LoanResponseDto dto = new LoanResponseDto();
        dto.setLoanId(e.getLoanId());
        dto.setAccountId(e.getAccountId());
        dto.setAmount(e.getAmount());
        dto.setInterestRate(e.getInterestRate());
        dto.setTerm(e.getTerm());
        dto.setStartDate(e.getStartDate());
        dto.setStatus(e.getStatus());
        return dto;
    }
}
