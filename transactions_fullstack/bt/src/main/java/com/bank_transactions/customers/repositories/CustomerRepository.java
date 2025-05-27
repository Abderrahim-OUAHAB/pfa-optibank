package com.bank_transactions.customers.repositories;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.bank_transactions.customers.entities.Customer;

public interface CustomerRepository extends CassandraRepository<Customer, String> {
    void deleteByCustomerId(String email);
    Customer findByCustomerId(String email);
}
