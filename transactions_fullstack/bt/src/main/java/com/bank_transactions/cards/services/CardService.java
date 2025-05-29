package com.bank_transactions.cards.services;

import com.bank_transactions.cards.dtos.CardRequestDto;
import com.bank_transactions.cards.dtos.CardResponseDto;
import java.util.List;

public interface CardService {
    CardResponseDto create(CardRequestDto dto);
    List<CardResponseDto> getAll();
    CardResponseDto getCardByAccountId(String accountId);
    void deleteByAccountId(String accountId);
}
