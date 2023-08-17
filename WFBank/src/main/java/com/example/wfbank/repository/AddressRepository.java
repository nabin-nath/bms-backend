package com.example.wfbank.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.wfbank.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long>{

}

