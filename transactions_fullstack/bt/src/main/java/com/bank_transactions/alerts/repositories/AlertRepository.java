package com.bank_transactions.alerts.repositories;

import com.bank_transactions.alerts.entities.Alert;
import org.springframework.data.cassandra.repository.CassandraRepository;
public interface AlertRepository extends CassandraRepository<Alert, String> {}
