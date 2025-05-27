package com.bank_transactions.transactions.repositories;

import com.bank_transactions.transactions.entities.Transaction;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends CassandraRepository<Transaction, String> {

    @Query("SELECT * FROM transactions WHERE transaction_id = ?0")
    Transaction findByTransactionId(String transactionId);

    List<Transaction> findAll();
    @Query("SELECT * FROM transactions WHERE user_email = ?0 ALLOW FILTERING")
    List<Transaction> findByUserEmail(String userEmail);
}
