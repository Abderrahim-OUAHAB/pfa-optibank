package com.bank_transactions.cards.mappers;

import com.bank_transactions.cards.entities.Card;
import com.bank_transactions.cards.dtos.CardRequestDto;
import com.bank_transactions.cards.dtos.CardResponseDto;
import java.util.UUID;

public class CardMapper {
    public static Card toEntity(CardRequestDto dto) {
        Card e = new Card();
        e.setCardId(UUID.randomUUID().toString());
        e.setCardNumber(dto.getCardNumber());
        e.setExpiryDate(dto.getExpiryDate());
        e.setCvv(dto.getCvv());
        e.setType(dto.getType());
        e.setStatus(dto.getStatus());
        e.setAccountId(dto.getAccountId());
    
        return e;
    }
    public static CardResponseDto toResponse(Card e) {
        CardResponseDto dto = new CardResponseDto();
        dto.setCardId(e.getCardId());
        dto.setCardNumber(e.getCardNumber());
        dto.setExpiryDate(e.getExpiryDate());
        dto.setCvv(e.getCvv());
        dto.setType(e.getType());
        dto.setStatus(e.getStatus());
        dto.setAccountId(e.getAccountId());
        return dto;
    }
}
