package com.example.wfbank.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.wfbank.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	public List<Transaction> findByFromAccOrToAcc(long fromAccNo, long toAccNo);
	public List<Transaction> findByFromAccAndTimeStampBetweenOrToAccAndTimeStampBetweenOrderByTimeStamp(long fromAccNo, Date s, Date e, long toAccNo, Date start, Date end);
	@Query("SELECT t FROM Transaction t WHERE (t.fromAcc = :acc OR t.toAcc = :acc) AND( t.timeStamp BETWEEN :sd AND :ed) AND (:type ='' OR t.transType = :type)")
	public Page<Transaction> findByAccountBetweenDate(@Param("acc")long acc, @Param("sd")Date startDate,
			@Param("ed")Date endDate, @Param("type") String type,Pageable pageable);
}
