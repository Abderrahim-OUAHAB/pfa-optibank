package com.bank_transactions.beneficiaries.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.beneficiaries.dtos.BeneficiaryRequestDto;
import com.bank_transactions.beneficiaries.dtos.BeneficiaryResponseDto;
import com.bank_transactions.beneficiaries.services.BeneficiaryService;

@RestController
@RequestMapping("/beneficiarys")
@CrossOrigin("http://localhost:4200")
public class BeneficiaryController {
    @Autowired private BeneficiaryService service;

    @PostMapping
    public ResponseEntity<BeneficiaryResponseDto> create(@RequestBody BeneficiaryRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<BeneficiaryResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
