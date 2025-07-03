package com.bank_transactions.beneficiaries.dtos;

import lombok.Data;
import java.time.*;
import java.math.BigDecimal;
@Data
public class BeneficiaryResponseDto {
    private String beneficiaryId;
    private String accountNumber;
    private String bankDetails;
    private String userEmail;
}
