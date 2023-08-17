package com.example.wfbank.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.example.wfbank.model.Address;
import com.example.wfbank.repository.AddressRepository;

@Service
@Validated
public class AddressServiceImpl {
	
	private AddressRepository addressRepository;
	
	public AddressServiceImpl(AddressRepository addressRepository) {
		this.addressRepository = addressRepository;
	}
	
	
	public Address saveAddress(Address address) {
		// TODO Auto-generated method stub		
		return addressRepository.save(address);
	}

}
