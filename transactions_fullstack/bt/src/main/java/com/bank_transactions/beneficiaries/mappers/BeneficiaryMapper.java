package com.bank_transactions.beneficiaries.mappers;

import com.bank_transactions.beneficiaries.entities.Beneficiary;
import com.bank_transactions.beneficiaries.dtos.BeneficiaryRequestDto;
import com.bank_transactions.beneficiaries.dtos.BeneficiaryResponseDto;
import java.util.UUID;

public class BeneficiaryMapper {
    public static Beneficiary toEntity(BeneficiaryRequestDto dto) {
        Beneficiary e = new Beneficiary();
        e.setBeneficiaryId(UUID.randomUUID().toString());
        e.setAccountNumber(dto.getAccountNumber());
        e.setBankDetails(dto.getBankDetails());
        e.setUserEmail(dto.getUserEmail());
        return e;
    }
    public static BeneficiaryResponseDto toResponse(Beneficiary e) {
        BeneficiaryResponseDto dto = new BeneficiaryResponseDto();
        dto.setBeneficiaryId(e.getBeneficiaryId());
        dto.setAccountNumber(e.getAccountNumber());
        dto.setBankDetails(e.getBankDetails());
        dto.setUserEmail(e.getUserEmail());
        return dto;
    }
}
