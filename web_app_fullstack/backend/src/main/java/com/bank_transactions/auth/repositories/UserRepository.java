package com.bank_transactions.auth.repositories;

import java.util.Optional;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bank_transactions.auth.entities.User;

public interface UserRepository extends CassandraRepository<User, String> {
    Optional<User> findByEmail(String email);
    void deleteByEmail(String email);
  @Query("UPDATE users SET status = :status WHERE email = :email")
void updateUserStatus(@Param("email") String email, @Param("status") String status);
}
