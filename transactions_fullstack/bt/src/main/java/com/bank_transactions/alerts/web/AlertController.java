package com.bank_transactions.alerts.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank_transactions.alerts.dtos.AlertRequestDto;
import com.bank_transactions.alerts.dtos.AlertResponseDto;
import com.bank_transactions.alerts.services.AlertService;

@RestController
@RequestMapping("/alerts")
@CrossOrigin("http://localhost:4200")
public class AlertController {
    @Autowired private AlertService service;

    @PostMapping
    public ResponseEntity<AlertResponseDto> create(@RequestBody AlertRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<AlertResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }
}
