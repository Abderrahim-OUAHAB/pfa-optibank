package com.bank_transactions.cards.repositories;

import com.bank_transactions.cards.entities.Card;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
public interface CardRepository extends CassandraRepository<Card, String> {

    @Query("SELECT * FROM cards WHERE accountId = ?0 ALLOW FILTERING")
    Card findCardByAccountId(String accountId);

    default void deleteByAccountId(String accountId) {
      deleteById(findCardByAccountId(accountId).getCardId());
    }
}
