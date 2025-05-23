package com.bank_transactions.exchange_rates.repositories;

import com.bank_transactions.exchange_rates.entities.ExchangeRate;
import org.springframework.data.cassandra.repository.CassandraRepository;
public interface ExchangeRateRepository extends CassandraRepository<ExchangeRate, String> {}
