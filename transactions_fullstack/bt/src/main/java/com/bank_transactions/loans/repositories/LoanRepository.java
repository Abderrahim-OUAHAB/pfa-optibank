package com.bank_transactions.loans.repositories;

import com.bank_transactions.loans.entities.Loan;
import org.springframework.data.cassandra.repository.CassandraRepository;
public interface LoanRepository extends CassandraRepository<Loan, String> {}
