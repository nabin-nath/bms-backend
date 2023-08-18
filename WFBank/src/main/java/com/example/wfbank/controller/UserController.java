package com.example.wfbank.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.example.wfbank.util.Validator;
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
	@Autowired private BCryptPasswordEncoder passwordEncoder;
	private ObjectMapper objectMapper;
	
	public UserController(UserService UserService, AccountsService accountService,
			BCryptPasswordEncoder encoder) {
		super();
		this.UserService = UserService;
		this.accountService = accountService;
		this.passwordEncoder = encoder;
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
			if(!Validator.nonNullValidator(jsonNode.get("password")) ||
					!Validator.nonNullValidator(jsonNode.get("pin"))) {
				throw new Exception("Password and Null cant be empty");
			}
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
	/*
	@GetMapping
	public List<User> getAllUser(){
		return UserService.getAllUser();
	}
	*/

	// build get account by id REST API
	// http://localhost:8080/api/User
	@GetMapping
	public ResponseEntity<Object> getUserById(){
		User user;
		try {
			user = UserService.getCurrentUser();
		}
		catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		
		return new ResponseEntity<Object>(user, HttpStatus.OK);
	}
	
	// build update account REST API
	// http://localhost:8080/api/User
	@PutMapping
	public ResponseEntity<Object> updateUser(@RequestBody JsonNode jsonNode){
		User user;
		try{
			user = UserService.getCurrentUser();
			
			if(Validator.nonNullValidator(jsonNode.get("password"))) {
				String password = jsonNode.get("password").asText();
				String confirmPassword = jsonNode.get("confirmPassword").asText();
				if(password.equals(confirmPassword)) {
					user.setPassword(passwordEncoder.encode(password));
				}
			}
			
			if(Validator.nonNullValidator(jsonNode.get("pin"))) {
				String password = jsonNode.get("pin").asText();
				String confirmPassword = jsonNode.get("confirmPin").asText();
				if(password.equals(confirmPassword)) {
					user.setPin(passwordEncoder.encode(password));
				}
			}
			return new ResponseEntity<Object>(UserService.saveUser(user), HttpStatus.OK);
		}
		
		catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	// build delete account REST API
	// http://localhost:8080/api/User/1
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") long id){
		
		// delete account from DB
		UserService.deleteUser(id);
		
		return new ResponseEntity<String>("User deleted successfully!.", HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Map<String, String>> getUserId(@PathVariable("id")long id){
		Map<String,String> mp =new HashMap<>();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		if(accountService.existsById(id)) {
			try {
				long retId = UserService.findByAccount(id);
				mp.put("id", Long.toString(retId));
				status = HttpStatus.OK;
			}
			catch (Exception e) {
				mp.put("message", e.getMessage());
			}
		}
		else {
			mp.put("message", "Account Number Does Not Exist");
		}
		return new ResponseEntity<>(mp, status);
	}
}
