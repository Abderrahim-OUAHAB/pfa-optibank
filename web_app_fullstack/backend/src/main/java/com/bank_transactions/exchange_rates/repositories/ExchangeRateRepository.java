package com.bank_transactions.exchange_rates.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

import com.bank_transactions.exchange_rates.entities.ExchangeRate;
public interface ExchangeRateRepository extends CassandraRepository<ExchangeRate, String> {

        @Query("SELECT * FROM exchange_rates WHERE currency_from = ?0 AND currency_to = ?1 ALLOW FILTERING")
    ExchangeRate findByCurrencyFromAndCurrencyTo(String from, String to);

}
