package com.ishana.banking_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ishana.banking_management.dto.BankAccount;

@Repository
public interface BankRepository extends JpaRepository<BankAccount, Long> {

}
