package com.bank_transactions.payment_plans.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.payment_plans.dtos.PaymentPlanRequestDto;
import com.bank_transactions.payment_plans.dtos.PaymentPlanResponseDto;
import com.bank_transactions.payment_plans.services.PaymentPlanService;

@RestController
@RequestMapping("/paymentplans")
@CrossOrigin("http://localhost:4200")
public class PaymentPlanController {
    @Autowired private PaymentPlanService service;

    @PostMapping
    public ResponseEntity<PaymentPlanResponseDto> create(@RequestBody PaymentPlanRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<PaymentPlanResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
