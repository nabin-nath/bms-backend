package com.example.wfbank.service.impl;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.wfbank.exception.ResourceNotFoundException;
import com.example.wfbank.model.Accounts;
import com.example.wfbank.repository.AccountsRepository;


@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {
	@Mock
	private AccountsRepository accountsRepository;
	
	private AccountsServiceImpl accSer;
	
	@BeforeEach
	void setUp() {
		this.accSer = new AccountsServiceImpl(accountsRepository);
	}
	
	@Test
	@Disabled
	void saveAccounts(Accounts accounts) {
		// TODO Auto-generated method stub		
		verify(accountsRepository).save(accounts);
	}

	@Test
	void getAllAccounts() {
		// TODO Auto-generated method stub
		accSer.getAllAccounts();
		verify(accountsRepository).findAll();
	}

	@Test
	@Disabled
	void getAccountsById() {
		// TODO Auto-generated method stub
		long id = 74818464;
		accSer.getAccountsById(id);
		verify(accountsRepository).findById(id).orElseThrow(
				()-> new ResourceNotFoundException("Accounts", "Id", id));
	}

}
