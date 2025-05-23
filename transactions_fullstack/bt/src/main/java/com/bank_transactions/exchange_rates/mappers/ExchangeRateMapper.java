package com.bank_transactions.exchange_rates.mappers;

import com.bank_transactions.exchange_rates.entities.ExchangeRate;
import com.bank_transactions.exchange_rates.dtos.ExchangeRateRequestDto;
import com.bank_transactions.exchange_rates.dtos.ExchangeRateResponseDto;
import java.util.UUID;

public class ExchangeRateMapper {
    public static ExchangeRate toEntity(ExchangeRateRequestDto dto) {
        ExchangeRate e = new ExchangeRate();
        e.setRateId(UUID.randomUUID().toString());
        e.setCurrencyFrom(dto.getCurrencyFrom());
        e.setCurrencyTo(dto.getCurrencyTo());
        e.setRate(dto.getRate());
        return e;
    }
    public static ExchangeRateResponseDto toResponse(ExchangeRate e) {
        ExchangeRateResponseDto dto = new ExchangeRateResponseDto();
        dto.setRateId(e.getRateId());
        dto.setCurrencyFrom(e.getCurrencyFrom());
        dto.setCurrencyTo(e.getCurrencyTo());
        dto.setRate(e.getRate());
        return dto;
    }
}
