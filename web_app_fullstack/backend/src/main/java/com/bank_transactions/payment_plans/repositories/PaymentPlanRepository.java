package com.bank_transactions.payment_plans.repositories;

import com.bank_transactions.payment_plans.entities.PaymentPlan;
import org.springframework.data.cassandra.repository.CassandraRepository;
public interface PaymentPlanRepository extends CassandraRepository<PaymentPlan, String> {}
