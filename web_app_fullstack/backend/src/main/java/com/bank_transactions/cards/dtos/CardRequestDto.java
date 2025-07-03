package com.bank_transactions.cards.dtos;

import lombok.Data;
import java.time.*;
import java.math.BigDecimal;
@Data
public class CardRequestDto {
    private String cardNumber;
    private LocalDate expiryDate;
    private String cvv;
    private String type;
    private String status;
    private String accountId;
}
