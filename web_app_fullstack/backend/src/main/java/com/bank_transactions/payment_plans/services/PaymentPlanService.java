package com.bank_transactions.payment_plans.services;

import com.bank_transactions.payment_plans.dtos.PaymentPlanRequestDto;
import com.bank_transactions.payment_plans.dtos.PaymentPlanResponseDto;
import java.util.List;

public interface PaymentPlanService {
    PaymentPlanResponseDto create(PaymentPlanRequestDto dto);
    List<PaymentPlanResponseDto> getAll();
}
