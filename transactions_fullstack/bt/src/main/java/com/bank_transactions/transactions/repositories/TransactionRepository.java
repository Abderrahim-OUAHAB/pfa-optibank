package com.bank_transactions.transactions.repositories;

import com.bank_transactions.transactions.entities.Transaction;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionRepository extends CassandraRepository<Transaction, UUID> {

    @Query("SELECT * FROM transactions WHERE transaction_id = ?0")
    Transaction findByTransactionId(String transactionId);
}
