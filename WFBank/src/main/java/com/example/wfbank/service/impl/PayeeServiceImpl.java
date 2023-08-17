package com.example.wfbank.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.wfbank.model.Payee;
import com.example.wfbank.repository.PayeeRepository;
import com.example.wfbank.service.PayeeService;

@Service
public class PayeeServiceImpl implements PayeeService {
	private PayeeRepository payeeRepository;

	public PayeeServiceImpl(PayeeRepository payeeRepository) {
		super();
		this.payeeRepository = payeeRepository;
	}
	public Payee savePayee(Payee payee) {
		// TODO Auto-generated method stub
		return payeeRepository.save(payee);
	}

	public List<Payee> getAllPayee() {
		// TODO Auto-generated method stub
		return payeeRepository.findAll();
	}

	public Payee getPayeeById(long id) {
		// TODO Auto-generated method stub
		return payeeRepository.findById(id).orElseThrow();
	}

	public Payee updatePayee(Payee payee, long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void deletePayee(long id) {
		// TODO Auto-generated method stub
		payeeRepository.findById(id);
		payeeRepository.deleteById(id);
	}
}
