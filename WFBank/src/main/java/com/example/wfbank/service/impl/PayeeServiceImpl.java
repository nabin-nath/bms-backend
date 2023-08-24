package com.example.wfbank.service.impl;

import java.util.List;
import java.util.Optional;

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
	
	public List<Payee> getPayeesByAccNumber(long accNumber){
		return payeeRepository.findByAccNumber(accNumber);
	}
	
	public List<Payee> getAllPayee() {
		// TODO Auto-generated method stub
		return payeeRepository.findAll();
	}

	public Payee getPayeeById(long id) {
		// TODO Auto-generated method stub
		return payeeRepository.findById(id).orElseThrow();
	}

	public void updatePayee(Payee payee, long id) {
		// TODO Auto-generated method stub
		Optional<Payee> optPayee = payeeRepository.findById(id);
		if(optPayee.isPresent()) {
			payee.setId(id);
			payeeRepository.save(payee);
		}
	}
	
	public void deletePayee(long id) {
		// TODO Auto-generated method stub
		payeeRepository.findById(id);
		payeeRepository.deleteById(id);
	}
	
	public boolean payeeExists(long accNo, long benNo) {
		if(payeeRepository.findByAccNumberAndBeneficiaryAccNumber(accNo, benNo)!=null) {
			return true;
		}
		return false;
	}
}
