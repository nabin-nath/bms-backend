package com.example.wfbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.wfbank.model.Accounts;
import com.example.wfbank.model.User;
import com.example.wfbank.service.AccountsService;
import com.example.wfbank.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired private UserService UserService;
	@Autowired private AccountsService accountService;
	private ObjectMapper objectMapper;
	
	public UserController(UserService UserService, AccountsService accountService) {
		super();
		this.UserService = UserService;
		this.accountService = accountService;
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	// build create account REST API
	@PostMapping()
	public ResponseEntity<String> saveUser(@RequestBody JsonNode jsonNode) throws JsonMappingException, JsonProcessingException, 
	Exception{
//		JsonNode jsonNode = objectMapper.readTree(requestBody);
		long userId;
		try {
			if(!jsonNode.has("accNumber")) {
				throw new Exception("accNumber not given");
			}
			Accounts account = accountService.getAccountsById(jsonNode.get("accNumber").asLong());
			User user = objectMapper.treeToValue(jsonNode, User.class);
			user.setAccount(account);
			userId = UserService.saveUser(user).getUserId();
		}
		catch (Exception e) {
			return new ResponseEntity<>("Error Message "+e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("User Created Succesfully \nUser Id is "+userId, HttpStatus.CREATED);
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
