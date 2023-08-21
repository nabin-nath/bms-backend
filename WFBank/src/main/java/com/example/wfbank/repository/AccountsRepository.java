package com.example.wfbank.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wfbank.model.Accounts;

public interface AccountsRepository extends JpaRepository<Accounts, Long>{

	List<Accounts> findAllByApprovedFalse();
	
}
