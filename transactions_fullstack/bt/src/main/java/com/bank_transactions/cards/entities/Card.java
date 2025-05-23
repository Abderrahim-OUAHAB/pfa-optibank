package com.bank_transactions.cards.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("cards")
public class Card {

    @PrimaryKey
    private String cardId;

    private String cardNumber;
    private LocalDate expiryDate;
    private String cvv;
    private String type;
    private String status;
    private String accountId;
}
