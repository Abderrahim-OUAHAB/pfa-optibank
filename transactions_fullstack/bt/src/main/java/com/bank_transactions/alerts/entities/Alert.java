package com.bank_transactions.alerts.entities;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.Data;

@Data
@Table("alerts")
public class Alert {

    @PrimaryKey
    private String alertId;
    private String accountId;
    private String type;
    private String message;
    private String severity;
    private String status;
}
