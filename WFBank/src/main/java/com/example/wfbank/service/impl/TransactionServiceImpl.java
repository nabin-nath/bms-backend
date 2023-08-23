package com.example.wfbank.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.wfbank.model.Transaction;
import com.example.wfbank.repository.TransactionRepository;
import com.example.wfbank.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService{
    private TransactionRepository TransactionRepository;
	
	public TransactionServiceImpl(TransactionRepository TransactionRepository) {
		this.TransactionRepository = TransactionRepository;
	}

	@Override
	public Transaction saveTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		return TransactionRepository.save(transaction);
	}

//	@Override
//	public List<Transaction> findByUserId(long AccNo) {
//		// TODO Auto-generated method stub
//		return TransactionRepository.findByFromAccNumberOrToAccNumber(AccNo,AccNo);
//	}

	@Override
	public List<Transaction> getAllTransaction() {
		// TODO Auto-generated method stub
		return TransactionRepository.findAll();
	}

	@Override
	public List<Transaction> findByUserId(long AccNo) {
		// TODO Auto-generated method stub
		return TransactionRepository.findByFromAccOrToAcc(AccNo, AccNo);
	}
	
	@Override
	public List<Transaction> getBetweenTimeStamp(long AccNo, Date start, Date end) {
		return TransactionRepository.findByFromAccAndTimeStampBetweenOrToAccAndTimeStampBetweenOrderByTimeStamp(AccNo, start, end, AccNo, start, end);
	}
	
	@Override
	public Page<Transaction> getBetweenTimeStamp(long AccNo, Date start, Date end,String type, Pageable pageable) {
		return TransactionRepository.findByAccountBetweenDate(AccNo, start, end,type, pageable);
	}
}
