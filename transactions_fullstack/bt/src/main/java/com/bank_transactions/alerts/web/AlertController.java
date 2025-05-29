package com.bank_transactions.alerts.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping("/create")
    public ResponseEntity<AlertResponseDto> create(@RequestBody AlertRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/")
    public ResponseEntity<List<AlertResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/update/{alertId}/{status}")
    public ResponseEntity<Map<String, String>> updateAlertStatus(@PathVariable String alertId, @PathVariable String status) {
         service.updateAlertStatus(alertId, status);
         return  ResponseEntity.ok(Map.of("message", "Alert status updated"));
    }
    @GetMapping("/find/{accountId}")
    public ResponseEntity<List<AlertResponseDto>> findAlertsByAccountId(@PathVariable String accountId) {
        return ResponseEntity.ok(service.findAlertsByAccountId(accountId));
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<Map<String, String>> deleteByAccountId(@PathVariable String accountId) {
        service.deleteByAccountId(accountId);
        return ResponseEntity.ok(Map.of("message", "Alerts deleted"));
    }
}
