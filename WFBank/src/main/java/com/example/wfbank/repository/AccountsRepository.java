package com.example.wfbank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wfbank.model.Accounts;

public interface AccountsRepository extends JpaRepository<Accounts, Long>{
	
}
