package com.bank_transactions.cards.repositories;

import com.bank_transactions.cards.entities.Card;
import org.springframework.data.cassandra.repository.CassandraRepository;
public interface CardRepository extends CassandraRepository<Card, String> {}
