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
import com.example.wfbank.service.impl.OtpService;
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
	@Autowired private OtpService otpService;
	private ObjectMapper objectMapper;
	
	public UserController(UserService UserService, AccountsService accountService,
			BCryptPasswordEncoder encoder, OtpService otpService) {
		super();
		this.UserService = UserService;
		this.accountService = accountService;
		this.passwordEncoder = encoder;
		this.otpService = otpService;
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	// build create account REST API
	@PostMapping()
	public ResponseEntity<Map<String,String>> saveUser(@RequestBody JsonNode jsonNode) throws JsonMappingException, JsonProcessingException, 
	Exception{
//		JsonNode jsonNode = objectMapper.readTree(requestBody);
		long userId;
		Map<String,String> mp = new HashMap<>();
		try {
			String [] fields = {"accNumber", "password", "pin", "confirmPassword", "confirmPin", "otp"}; 
			if(!Validator.nonNullFieldsValidator(jsonNode, fields))
				throw new Exception("Fields cant be empty");
			Integer otp = Integer.valueOf(jsonNode.get("otp").asInt());
			Accounts account = accountService.getAccountsById(jsonNode.get("accNumber").asLong());
			String password = jsonNode.get("password").asText(), 
					confirmPassword = jsonNode.get("confirmPassword").asText(), pin = jsonNode.get("pin").asText(),
					confirmPin = jsonNode.get("confirmPin").asText();
			
			if(!password.equals(confirmPassword) || !pin.equals(confirmPin)) {
				throw new Exception("Password and pin must match with confirmation");
			}
			
			if(UserService.existByAccount(account.getAccNumber())) {
				throw new Exception("User For this Account Already Registered");
			}
			
			if(!otpService.validateOTP(jsonNode.get("accNumber").asText()+":create", otp));
			
			User user = objectMapper.treeToValue(jsonNode, User.class);
			user.setAccount(account);
			user.setPassword(passwordEncoder.encode(password));
			user.setPin(passwordEncoder.encode(pin));
			userId = UserService.saveUser(user).getUserId();
			mp.put("userId", Long.toString(userId));
			mp.put("message", "User Id Created for given Account Number");
		}
		catch (Exception e) {
			mp.put("message", e.getMessage());
			return new ResponseEntity<>(mp,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(mp, HttpStatus.CREATED);
	}
	

	// build get current user detail REST API
	// http://localhost:8080/api/User
	@GetMapping
	public ResponseEntity<Object> getUser(){
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
				else
					throw new Exception("Password and Confirm Password Does Not match");
			}
			
			if(Validator.nonNullValidator(jsonNode.get("pin"))) {
				String password = jsonNode.get("pin").asText();
				String confirmPassword = jsonNode.get("confirmPin").asText();
				if(password.equals(confirmPassword)) {
					user.setPin(passwordEncoder.encode(password));
				}
				else
					throw new Exception("Pin and Confirm Pin Does Not match");
			}
			return new ResponseEntity<Object>(UserService.saveUser(user), HttpStatus.OK);
		}
		
		catch (Exception e) {
			return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	@PutMapping("/forget-password")
	public ResponseEntity<Map<String,String>> forgetPassword(@RequestBody JsonNode jsonNode){
		User user;
		Map<String,String> mp = new HashMap<>();
		try{
			if(!jsonNode.has("userId"))
				throw new Exception ("id not provided");
			long id = jsonNode.get("userId").asLong();
			user = UserService.getUserById(id);
			if(user==null)
				throw new Exception("user with given id does not exist");
			Integer otp = Integer.parseInt(jsonNode.get("otp").asText());
			long accNumber = user.getAccount().getAccNumber();
			boolean validated = otpService.validateOTP(Long.toString(accNumber)+":update", otp);
			if(!validated)
				throw new Exception("OTP does not match");
			
			if(Validator.nonNullValidator(jsonNode.get("password"))) {
				String password = jsonNode.get("password").asText();
				String confirmPassword = jsonNode.get("confirmPassword").asText();
				if(password.equals(confirmPassword)) {
					user.setPassword(passwordEncoder.encode(password));
					UserService.saveUser(user);
				}
				else
					throw new Exception("Password and Confirm Password Does Not match");
			}
			else
				throw new Exception("Password Can't Be empty");
			mp.put("message", "Password Reset Successful");
			return new ResponseEntity<>(mp, HttpStatus.OK);
		}
		
		catch (Exception e) {
			mp.put("message",e.getMessage());
			return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
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
	
	@GetMapping("/id")
	public ResponseEntity<Map<String, String>> getUserAccNumber(@RequestBody JsonNode jsonNode){
		Map<String,String> mp =new HashMap<>();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		long id;
		Integer otp;
		
		try {
			id = jsonNode.get("accNumber").asLong();
			otp = Integer.parseInt(jsonNode.get("otp").asText());
		}
		catch (Exception e) {
			mp.put("message", "Invalid Account Number or OTP");
			return new ResponseEntity<>(mp,status);
		}
		
		if(accountService.existsById(id)) {
			try {
				long retId = UserService.findByAccount(id);
				boolean validated = otpService.validateOTP(Long.toString(id)+":find", otp);
				if(validated) {
					mp.put("id", Long.toString(retId));
					status = HttpStatus.OK;
				}
				else {
					mp.put("message", "Otps Does Not Match");
				}
					
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
	
	@GetMapping("/otp-gen/{type}")
	public ResponseEntity<Map<String,String>> generateOtp(@RequestBody JsonNode jsonNode,
			@PathVariable("type") String type){
		Map<String, String>mp = new HashMap<>();
		HttpStatus status = HttpStatus.BAD_REQUEST;
		long id;
		try {
			if(type.equals("update")) {
				long userId = jsonNode.get("userId").asLong();
				id = UserService.getUserById(userId).getAccount().getAccNumber();
			}
			else {
				id = jsonNode.get("accNumber").asLong();
				if(!accountService.existsById(id)) {
					throw new Exception();
				}
			}
			String mail = accountService.getAccountsById(id).getEmail();
			otpService.generateOtp(mail, Long.toString(id)+":"+type);
		}
		catch (Exception e) {
			mp.put("message", "Invalid Credentials Given");
			return new ResponseEntity<>(mp,status);
		}
		mp.put("message", "OTP Sent to mail " );
		return new ResponseEntity<>(mp,HttpStatus.OK);
		
	}
}
