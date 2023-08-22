      package com.example.wfbank.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.wfbank.exception.ResourceNotFoundException;
import com.example.wfbank.model.User;
import com.example.wfbank.repository.UserRepository;
import com.example.wfbank.service.UserService;
//import com.example.wfbank.util.Validator;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository UserRepository;
	
	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		return UserRepository.save(user);
	}

	@Override
	public User updateUser(User user) {
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
	public User getCurrentUser() {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		long userId = Long.parseLong(userName);
		return getUserById(userId);	
	}

	@Override
	public void deleteUser(long id) {
		// TODO Auto-generated method stub
		UserRepository.findById(id).orElseThrow(
				()-> new ResourceNotFoundException("User", "Id", id));
		UserRepository.deleteById(id);
	}
	
	@Override
	public long findByAccount(long id) {
		return UserRepository.findByAccountAccNumber(id).getUserId();
	}

	@Override
	public User findUserByAccount(long id) {
		return UserRepository.findByAccountAccNumber(id);
	}

	@Override
	public boolean existByAccount(long id) {
		// TODO Auto-generated method stub
		return UserRepository.existsByAccountAccNumber(id);
	}

}

