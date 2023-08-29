package com.example.wfbank.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.wfbank.model.Transaction;

@SpringBootTest
public class TransactionRepositoryTest {

	@Autowired
	private TransactionRepository transRepo;
	
	@Test
	void findByFromAccOrToAcc() {
		long AccNo = 48716984;
		List<Transaction> ts = transRepo.findByFromAccOrToAcc(AccNo,AccNo);
		boolean flag = true;
		for(Transaction t : ts) {
			if(t.getFromAcc()==AccNo || t.getToAcc()==AccNo) {
				continue;
			}
			else {
				flag = false;
				break;
			}
		}
		assertThat(flag).isEqualTo(true);
	}
}
