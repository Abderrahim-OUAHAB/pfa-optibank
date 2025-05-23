package com.bank_transactions.accounts.repositories;

import com.bank_transactions.accounts.entities.Account;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface AccountRepository extends CassandraRepository<Account, String> {
}
