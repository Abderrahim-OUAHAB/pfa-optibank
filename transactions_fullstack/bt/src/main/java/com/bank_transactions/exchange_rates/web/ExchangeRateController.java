package com.bank_transactions.exchange_rates.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.exchange_rates.dtos.ExchangeRateRequestDto;
import com.bank_transactions.exchange_rates.dtos.ExchangeRateResponseDto;
import com.bank_transactions.exchange_rates.mappers.ExchangeRateMapper;
import com.bank_transactions.exchange_rates.services.ExchangeRateService;

@RestController
@RequestMapping("/exchangerates")
@CrossOrigin("http://localhost:4200")
public class ExchangeRateController {
    @Autowired private ExchangeRateService service;

    @PostMapping("/create")
    public ResponseEntity<ExchangeRateResponseDto> create(@RequestBody ExchangeRateRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/")
    public ResponseEntity<List<ExchangeRateResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
 @PutMapping("/update/{rateId}")
    public ResponseEntity<Map<String, String>> update(@PathVariable String rateId, @RequestBody ExchangeRateRequestDto dto) {
        service.updateRate(rateId,dto);
        return ResponseEntity.ok(Map.of("message", "Rate updated"));
 }
    @DeleteMapping("/delete/{rateId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String rateId) {
        service.deleteRate(rateId);
        return ResponseEntity.ok(Map.of("message", "Rate deleted"));
    }

    @GetMapping("/convert/{from}/{to}")
    public ResponseEntity<ExchangeRateResponseDto> getRateByCurrencies(@PathVariable String from, @PathVariable String to) {
        return ResponseEntity.ok(ExchangeRateMapper.toResponse(service.getRateByCurrencies(from, to)));
    }
}
