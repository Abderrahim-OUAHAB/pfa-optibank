package com.bank_transactions.beneficiaries.repositories;

import com.bank_transactions.beneficiaries.entities.Beneficiary;
import org.springframework.data.cassandra.repository.CassandraRepository;
public interface BeneficiaryRepository extends CassandraRepository<Beneficiary, String> {}
