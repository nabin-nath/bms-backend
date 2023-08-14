package com.example.wfbank.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wfbank.model.User;
import com.example.wfbank.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	private UserService UserService;

	public UserController(UserService UserService) {
		super();
		this.UserService = UserService;
	}
	
	// build create account REST API
	@PostMapping()
	public ResponseEntity<User> saveUser(@RequestBody User user){
		return new ResponseEntity<User>(UserService.saveUser(user), HttpStatus.CREATED);
	}
	
	// build get all User REST API
	@GetMapping
	public List<User> getAllUser(){
		return UserService.getAllUser();
	}

	// build get account by id REST API
	// http://localhost:8080/api/User/1
	@GetMapping("{id}")
	public ResponseEntity<User> getUserById(@PathVariable("id") long userId){
		return new ResponseEntity<User>(UserService.getUserById(userId), HttpStatus.OK);
	}
	
	// build update account REST API
	// http://localhost:8080/api/User/1
	@PutMapping("{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") long id
												  ,@RequestBody User user){
		return new ResponseEntity<User>(UserService.updateUser(user, id), HttpStatus.OK);
	}
	
	// build delete account REST API
	// http://localhost:8080/api/User/1
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") long id){
		
		// delete account from DB
		UserService.deleteUser(id);
		
		return new ResponseEntity<String>("User deleted successfully!.", HttpStatus.OK);
	}
	
}
