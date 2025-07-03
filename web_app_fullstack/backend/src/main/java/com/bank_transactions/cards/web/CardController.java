package com.bank_transactions.cards.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.cards.dtos.CardRequestDto;
import com.bank_transactions.cards.dtos.CardResponseDto;
import com.bank_transactions.cards.services.CardService;

@RestController
@RequestMapping("/cards")
@CrossOrigin("http://localhost:4200")
public class CardController {
    @Autowired private CardService service;

    @PostMapping("/create")
    public ResponseEntity<CardResponseDto> create(@RequestBody CardRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/")
    public ResponseEntity<List<CardResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/find/{accountId}")
    public ResponseEntity<CardResponseDto> getCardByAccountId(@PathVariable String accountId) {
        return ResponseEntity.ok(service.getCardByAccountId(accountId));
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<Map<String, String>> deleteByAccountId(@PathVariable String accountId) {
        service.deleteByAccountId(accountId);
        return ResponseEntity.ok(Map.of("message", "Card deleted"));
    }
}
