package com.bank_transactions.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.config.SessionBuilderConfigurer;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "spark_streams";
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.CREATE_IF_NOT_EXISTS;
    }

    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {
        return Collections.singletonList(
            CreateKeyspaceSpecification.createKeyspace(getKeyspaceName())
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication(1L)
        );
    }

    @Override
    public String[] getEntityBasePackages() {
        return new String[]{
        "com.bank_transactions.auth.entities",
        "com.bank_transactions.transactions.entities",
        "com.bank_transactions.audit_logs.entities",
        "com.bank_transactions.beneficiaries.entities",
        "com.bank_transactions.accounts.entities",
        "com.bank_transactions.cards.entities",
        "com.bank_transactions.alerts.entities",
        "com.bank_transactions.loans.entities",
        "com.bank_transactions.payment_plans.entities",
        "com.bank_transactions.exchange_rates.entities", 
        "com.bank_transactions.customers.entities",
    };
    }
@Override
protected SessionBuilderConfigurer getSessionBuilderConfigurer() {
    return cqlSessionBuilder -> cqlSessionBuilder
        .withConfigLoader(DriverConfigLoader.programmaticBuilder()
            .withDuration(DefaultDriverOption.CONNECTION_CONNECT_TIMEOUT, Duration.ofSeconds(120))
            .withDuration(DefaultDriverOption.REQUEST_TIMEOUT, Duration.ofSeconds(120))
            .build());
}
    
}