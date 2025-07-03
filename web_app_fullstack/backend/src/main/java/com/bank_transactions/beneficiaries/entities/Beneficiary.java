package com.bank_transactions.beneficiaries.entities;

import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@Table("beneficiaries")
public class Beneficiary {

    @PrimaryKey
    private String beneficiaryId;

    private String accountNumber;
    private String bankDetails;
    private String userEmail;
}
