package com.ishana.banking_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ishana.banking_management.dto.Management;

@Repository
public interface ManagementRepository extends JpaRepository<Management, Integer> {

	Management findByEmail(String email);

	
            
}