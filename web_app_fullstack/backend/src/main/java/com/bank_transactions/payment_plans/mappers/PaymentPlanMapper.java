package com.bank_transactions.payment_plans.mappers;

import com.bank_transactions.payment_plans.entities.PaymentPlan;
import com.bank_transactions.payment_plans.dtos.PaymentPlanRequestDto;
import com.bank_transactions.payment_plans.dtos.PaymentPlanResponseDto;
import java.util.UUID;

public class PaymentPlanMapper {
    public static PaymentPlan toEntity(PaymentPlanRequestDto dto) {
        PaymentPlan e = new PaymentPlan();
        e.setPlanId(UUID.randomUUID().toString());
        e.setAmount(dto.getAmount());
        e.setDueDate(dto.getDueDate());
        e.setStatus(dto.getStatus());
        e.setLoanId(dto.getLoanId());
        return e;
    }
    public static PaymentPlanResponseDto toResponse(PaymentPlan e) {
        PaymentPlanResponseDto dto = new PaymentPlanResponseDto();
        dto.setPlanId(e.getPlanId());
        dto.setAmount(e.getAmount());
        dto.setDueDate(e.getDueDate());
        dto.setStatus(e.getStatus());
        dto.setLoanId(e.getLoanId());
        return dto;
    }
}
