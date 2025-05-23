package com.bank_transactions.exchange_rates.services;

import com.bank_transactions.exchange_rates.dtos.ExchangeRateRequestDto;
import com.bank_transactions.exchange_rates.dtos.ExchangeRateResponseDto;
import java.util.List;

public interface ExchangeRateService {
    ExchangeRateResponseDto create(ExchangeRateRequestDto dto);
    List<ExchangeRateResponseDto> getAll();
}
