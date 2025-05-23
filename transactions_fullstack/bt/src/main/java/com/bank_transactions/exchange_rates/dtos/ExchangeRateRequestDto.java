package com.bank_transactions.exchange_rates.dtos;

import lombok.Data;
import java.time.*;
import java.math.BigDecimal;
@Data
public class ExchangeRateRequestDto {
    private String currencyFrom;
    private String currencyTo;
    private double rate;
}
