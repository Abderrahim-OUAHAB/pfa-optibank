package com.bank_transactions.accounts.repositories;

import com.bank_transactions.accounts.entities.Account;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;

public interface AccountRepository extends CassandraRepository<Account, String> {
       // Return Account objects with only accountId populated
    @Query("SELECT accountId FROM accounts WHERE customerId = ?0 ALLOW FILTERING")
    Account findAccountsByCustomerId(String customerId);
    
    default void deleteByCustomerId(String customerId) {
        String id= findAccountsByCustomerId(customerId).getAccountId();

            deleteById(id);
    }
}
