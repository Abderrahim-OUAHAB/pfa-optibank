package com.bank_transactions.exchange_rates.services;

import com.bank_transactions.exchange_rates.dtos.ExchangeRateRequestDto;
import com.bank_transactions.exchange_rates.dtos.ExchangeRateResponseDto;
import com.bank_transactions.exchange_rates.entities.ExchangeRate;

import java.util.List;

public interface ExchangeRateService {
    ExchangeRateResponseDto create(ExchangeRateRequestDto dto);
    List<ExchangeRateResponseDto> getAll();
    
         void deleteRate(String rateId) ;
        void updateRate(String rateId, ExchangeRateRequestDto dto) ;
         ExchangeRate getRateByCurrencies(String from, String to) ;
}
