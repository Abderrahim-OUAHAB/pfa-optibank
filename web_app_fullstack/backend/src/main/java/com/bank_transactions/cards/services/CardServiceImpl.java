package com.bank_transactions.cards.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bank_transactions.cards.dtos.CardRequestDto;
import com.bank_transactions.cards.dtos.CardResponseDto;
import com.bank_transactions.cards.mappers.CardMapper;
import com.bank_transactions.cards.repositories.CardRepository;

@Service
public class CardServiceImpl implements CardService {
    @Autowired private CardRepository repo;
    public CardResponseDto create(CardRequestDto dto) {
        return CardMapper.toResponse(repo.save(CardMapper.toEntity(dto)));
    }
    public List<CardResponseDto> getAll() {
        return repo.findAll().stream().map(CardMapper::toResponse).collect(Collectors.toList());
    }
    @Override
    public CardResponseDto getCardByAccountId(String accountId) {
       return CardMapper.toResponse(repo.findCardByAccountId(accountId));
    }
    @Override
    public void deleteByAccountId(String accountId) {
        repo.deleteByAccountId(accountId);
    }

}
