package com.example.wfbank.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.wfbank.model.Accounts;

@SpringBootTest
public class AccountRepositoryTest {
	@Autowired
	private AccountsRepository accountRepository;
	
	@Test
	void findAllByApprovedFalse() {
		List<Accounts> accounts = accountRepository.findAllByApprovedFalse();
		boolean flag = false;
		for(Accounts acc : accounts) {
			if(acc.getApproved()) {
				flag = true;
			}
		}
		assertThat(flag).isFalse();
	}
}
