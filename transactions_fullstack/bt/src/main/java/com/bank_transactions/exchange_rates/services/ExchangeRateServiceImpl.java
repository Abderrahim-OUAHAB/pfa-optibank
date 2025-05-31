package com.bank_transactions.exchange_rates.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_transactions.exchange_rates.dtos.ExchangeRateRequestDto;
import com.bank_transactions.exchange_rates.dtos.ExchangeRateResponseDto;
import com.bank_transactions.exchange_rates.entities.ExchangeRate;
import com.bank_transactions.exchange_rates.mappers.ExchangeRateMapper;
import com.bank_transactions.exchange_rates.repositories.ExchangeRateRepository;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {
    @Autowired private ExchangeRateRepository repo;

    public ExchangeRateResponseDto create(ExchangeRateRequestDto dto) {
        return ExchangeRateMapper.toResponse(repo.save(ExchangeRateMapper.toEntity(dto)));
    }
    public List<ExchangeRateResponseDto> getAll() {
        return repo.findAll().stream().map(ExchangeRateMapper::toResponse).collect(Collectors.toList());
    }

        public void deleteRate(String rateId) {
        repo.deleteById(rateId);
    }

    public ExchangeRate getRateByCurrencies(String from, String to) {
        return repo.findByCurrencyFromAndCurrencyTo(from, to);
    }
    @Override
    public void updateRate(String rateId, ExchangeRateRequestDto rate) {
        ExchangeRate e = repo.findById(rateId).orElse(null);
        e.setCurrencyFrom(rate.getCurrencyFrom());
        e.setCurrencyTo(rate.getCurrencyTo());
        e.setRate(rate.getRate());
        repo.save(e);
    }
}
