package com.bank_transactions.customers.repositories;

import com.bank_transactions.customers.entities.Customer;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface CustomerRepository extends CassandraRepository<Customer, String> {
}
