package com.example.wfbank.service.impl;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.wfbank.exception.ResourceNotFoundException;
import com.example.wfbank.model.User;
import com.example.wfbank.repository.UserRepository;
import com.example.wfbank.service.UserService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository UserRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setPin(passwordEncoder.encode(user.getPin()));
		return UserRepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		return UserRepository.findAll();
	}

	@Override
	public User getUserById(long id) {
		// TODO Auto-generated method stub
		return UserRepository.findById(id).orElseThrow(
				()-> new ResourceNotFoundException("User", "Id", id));
	}

	@Override
	public User updateUser(User user, long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(long id) {
		// TODO Auto-generated method stub
		UserRepository.findById(id).orElseThrow(
				()-> new ResourceNotFoundException("User", "Id", id));
		UserRepository.deleteById(id);
	}

}

