package com.example.wfbank.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.wfbank.model.Admins;

public interface AdminRepository extends JpaRepository<Admins, Long> {
	

}
