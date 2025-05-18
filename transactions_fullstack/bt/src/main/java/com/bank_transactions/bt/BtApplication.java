package com.bank_transactions.bt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
    "com.bank_transactions.transactions.repositories"
})

public class BtApplication {
    public static void main(String[] args) {
        SpringApplication.run(BtApplication.class, args);
    }
}