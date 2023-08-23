package com.example.wfbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wfbank.model.Transaction;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	public List<Transaction> findByFromAccOrToAcc(long fromAccNo, long toAccNo);
	public List<Transaction> findByFromAccAndTimeStampBetweenOrToAccAndTimeStampBetweenOrderByTimeStamp(long fromAccNo, Date s, Date e, long toAccNo, Date start, Date end);
}
