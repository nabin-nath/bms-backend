package com.example.wfbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wfbank.model.Payee;

public interface PayeeRepository extends JpaRepository<Payee, Long> {

}
