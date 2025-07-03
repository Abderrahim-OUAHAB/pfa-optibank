package com.bank_transactions.exchange_rates.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("exchange_rates")
public class ExchangeRate {

    @PrimaryKey
    private String rateId;

    private String currencyFrom;
    private String currencyTo;
    private double rate;
}
