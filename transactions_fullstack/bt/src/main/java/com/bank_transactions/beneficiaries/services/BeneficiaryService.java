package com.bank_transactions.beneficiaries.services;

import com.bank_transactions.beneficiaries.dtos.BeneficiaryRequestDto;
import com.bank_transactions.beneficiaries.dtos.BeneficiaryResponseDto;
import java.util.List;

public interface BeneficiaryService {
    BeneficiaryResponseDto create(BeneficiaryRequestDto dto);
    List<BeneficiaryResponseDto> getAll();
}
