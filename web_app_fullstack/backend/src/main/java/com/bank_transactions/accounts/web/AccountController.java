package com.bank_transactions.accounts.web;

import com.bank_transactions.accounts.dtos.AccountRequestDto;
import com.bank_transactions.accounts.dtos.AccountResponseDto;
import com.bank_transactions.accounts.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/accounts")
@CrossOrigin("http://localhost:4200")
public class AccountController {

    @Autowired
    private AccountService service;

    @PostMapping("/create")
    public ResponseEntity<AccountResponseDto> create(@RequestBody AccountRequestDto dto) {
        return ResponseEntity.ok(service.create(dto));
    }

    @GetMapping("/")
    public ResponseEntity<List<AccountResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable String customerId) {
        service.deleteByCustomerId(customerId);
        return ResponseEntity.ok(Map.of("message", "Account deleted"));
    }

    @GetMapping("/find/{customerId}")
    public ResponseEntity<AccountResponseDto> findAccountsByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(service.findAccountsByCustomerId(customerId));
    }
    @PutMapping("/update/{accountId}/{balance}")
    public ResponseEntity<Map<String, String>> updateBalance(@PathVariable String accountId, @PathVariable Double balance) {
        service.updateBalance(accountId, balance);
        return ResponseEntity.ok(Map.of("message", "Balance updated"));
    }
}
