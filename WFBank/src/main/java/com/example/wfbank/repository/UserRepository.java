package com.example.wfbank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wfbank.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	User findByAccountAccNumber(long accNumber);
}
