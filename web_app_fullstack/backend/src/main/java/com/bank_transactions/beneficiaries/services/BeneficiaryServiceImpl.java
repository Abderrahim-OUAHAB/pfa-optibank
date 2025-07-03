package com.bank_transactions.beneficiaries.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_transactions.beneficiaries.dtos.BeneficiaryRequestDto;
import com.bank_transactions.beneficiaries.dtos.BeneficiaryResponseDto;
import com.bank_transactions.beneficiaries.mappers.BeneficiaryMapper;
import com.bank_transactions.beneficiaries.repositories.BeneficiaryRepository;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {
    @Autowired private BeneficiaryRepository repo;

    public BeneficiaryResponseDto create(BeneficiaryRequestDto dto) {
        return BeneficiaryMapper.toResponse(repo.save(BeneficiaryMapper.toEntity(dto)));
    }
    public List<BeneficiaryResponseDto> getAll() {
        return repo.findAll().stream().map(BeneficiaryMapper::toResponse).collect(Collectors.toList());
    }
}
