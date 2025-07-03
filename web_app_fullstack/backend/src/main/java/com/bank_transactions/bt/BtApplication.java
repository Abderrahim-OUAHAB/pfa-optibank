package com.bank_transactions.bt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
@SpringBootApplication(
    scanBasePackages = "com.bank_transactions",
exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@EnableCassandraRepositories(basePackages = {
    "com.bank_transactions.auth.repositories",
    "com.bank_transactions.transactions.repositories",
    "com.bank_transactions.accounts.repositories",
    "com.bank_transactions.beneficiaries.repositories",
    "com.bank_transactions.alerts.repositories",
    "com.bank_transactions.audit_logs.repositories",
    "com.bank_transactions.cards.repositories",
    "com.bank_transactions.customers.repositories",
    "com.bank_transactions.loans.repositories",
    "com.bank_transactions.payment_plans.repositories",
    "com.bank_transactions.exchange_rates.repositories"
})

public class BtApplication {
    public static void main(String[] args) {
        SpringApplication.run(BtApplication.class, args);
    }
}