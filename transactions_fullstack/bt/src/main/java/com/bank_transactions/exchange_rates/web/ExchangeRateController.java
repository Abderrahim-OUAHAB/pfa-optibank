package com.bank_transactions.exchange_rates.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.exchange_rates.dtos.ExchangeRateRequestDto;
import com.bank_transactions.exchange_rates.dtos.ExchangeRateResponseDto;
import com.bank_transactions.exchange_rates.services.ExchangeRateService;

@RestController
@RequestMapping("/exchangerates")
@CrossOrigin("http://localhost:4200")
public class ExchangeRateController {
    @Autowired private ExchangeRateService service;

    @PostMapping
    public ResponseEntity<ExchangeRateResponseDto> create(@RequestBody ExchangeRateRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<ExchangeRateResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
