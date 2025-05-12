package com.bank_transactions.auth.repositories;

import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;

import com.bank_transactions.auth.entities.User;

public interface UserRepository extends CassandraRepository<User, String> {
    Optional<User> findByEmail(String email);
}
