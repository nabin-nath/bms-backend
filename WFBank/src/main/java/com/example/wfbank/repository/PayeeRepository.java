package com.example.wfbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wfbank.model.Payee;

public interface PayeeRepository extends JpaRepository<Payee, Long> {
	public List<Payee> findByAccount_AccNumber(long accNumber);
}
