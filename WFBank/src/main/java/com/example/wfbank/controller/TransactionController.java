package com.example.wfbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wfbank.model.Transaction;
import com.example.wfbank.service.TransactionService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
	@Autowired private TransactionService TransactionService;
	private ObjectMapper objectMapper;
	
	public TransactionController(TransactionService TransactionService) {
		super();
		this.TransactionService = TransactionService;
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@PostMapping()
	public Transaction saveTransaction(@Validated @RequestBody Transaction transaction){
		return TransactionService.saveTransaction(transaction);
	}
	
	@GetMapping
	public List<Transaction> getAllTransaction(){
		return TransactionService.getAllTransaction();
	}
}
