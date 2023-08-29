package com.example.wfbank.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.wfbank.model.User;

@SpringBootTest
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepo;

	@Test
	void findByAccountAccNumber() {
		long accNumber = 53657678;
		User us = userRepo.findByAccountAccNumber(accNumber);
		boolean flag;
		if(us==null) {
			flag = true;
		}
		else { 
			flag = us.getAccount().getAccNumber()==accNumber?true:false;
		}
		assertThat(flag).isTrue();
	}
	
}
