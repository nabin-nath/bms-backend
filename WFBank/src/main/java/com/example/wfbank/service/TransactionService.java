package com.example.wfbank.service;

import java.util.Date;
import java.util.List;

import com.example.wfbank.model.Transaction;

public interface TransactionService {
    public Transaction saveTransaction(Transaction transaction);
    public List<Transaction> findByUserId(long AccNo);
    List<Transaction> getAllTransaction();
    List<Transaction> getBetweenTimeStamp(long AccNo, Date start, Date end);
}
