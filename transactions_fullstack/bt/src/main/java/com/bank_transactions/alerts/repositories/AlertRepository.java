package com.bank_transactions.alerts.repositories;

import com.bank_transactions.alerts.entities.Alert;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
public interface AlertRepository extends CassandraRepository<Alert, String> {

     @Query("SELECT * FROM alerts WHERE accountid = :accountId ALLOW FILTERING")
    List<Alert> findAlertsByAccountId(String accountId);
  
  default void deleteByAccountId(String accountId) {
    findAlertsByAccountId(accountId).forEach(alert -> deleteById(alert.getAlertId()));
}
    @Query("UPDATE alerts SET status = :status WHERE alertid = :alertId")
    void updateAlertStatus(String alertId, String status);
}
