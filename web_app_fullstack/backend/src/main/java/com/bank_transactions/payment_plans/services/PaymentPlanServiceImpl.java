package com.bank_transactions.payment_plans.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_transactions.payment_plans.dtos.PaymentPlanRequestDto;
import com.bank_transactions.payment_plans.dtos.PaymentPlanResponseDto;
import com.bank_transactions.payment_plans.mappers.PaymentPlanMapper;
import com.bank_transactions.payment_plans.repositories.PaymentPlanRepository;

@Service
public class PaymentPlanServiceImpl implements PaymentPlanService {
    @Autowired private PaymentPlanRepository repo;

    public PaymentPlanResponseDto create(PaymentPlanRequestDto dto) {
        return PaymentPlanMapper.toResponse(repo.save(PaymentPlanMapper.toEntity(dto)));
    }
    public List<PaymentPlanResponseDto> getAll() {
        return repo.findAll().stream().map(PaymentPlanMapper::toResponse).collect(Collectors.toList());
    }
}
