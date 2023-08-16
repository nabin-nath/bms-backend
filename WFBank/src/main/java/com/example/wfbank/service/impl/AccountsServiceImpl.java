package com.example.wfbank.service.impl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.wfbank.exception.ResourceNotFoundException;
import com.example.wfbank.model.Accounts;
import com.example.wfbank.repository.AccountsRepository;
import com.example.wfbank.service.AccountsService;

@Service
@Validated
public class AccountsServiceImpl implements AccountsService {

	
	private AccountsRepository accountsRepository;
	
	public AccountsServiceImpl(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}
	@Override
	public Accounts saveAccounts(@Valid Accounts accounts) {
		// TODO Auto-generated method stub		
		return accountsRepository.save(accounts);
	}

	@Override
	public List<Accounts> getAllAccounts() {
		// TODO Auto-generated method stub
		return accountsRepository.findAll();
	}

	@Override
	public Accounts getAccountsById(long id) {
		// TODO Auto-generated method stub
		return accountsRepository.findById(id).orElseThrow(
				()-> new ResourceNotFoundException("Accounts", "Id", id));
	}

	@Override
	public Accounts updateAccounts(Accounts Accounts, long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAccounts(long id) {
		// TODO Auto-generated method stub
		accountsRepository.findById(id).orElseThrow(
				()-> new ResourceNotFoundException("Accounts", "Id", id));
		accountsRepository.deleteById(id);
	}

}
