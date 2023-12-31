package com.example.wfbank.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wfbank.model.Accounts;
import com.example.wfbank.model.Address;
import com.example.wfbank.model.JobDetail;
import com.example.wfbank.model.User;
import com.example.wfbank.service.AccountsService;
import com.example.wfbank.service.UserService;
import com.example.wfbank.util.Validator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
	
	@Autowired private AccountsService accountService;
	@Autowired private UserService uService;
	private ObjectMapper objectMapper;
	private final Logger LOGGER = LoggerFactory.getLogger(AccountsController.class);
	public AccountsController(AccountsService accountService) {
		super();
		this.accountService = accountService;
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	private Accounts validateAccountsData(JsonNode jsonNode) throws Exception{
		JsonNode sameAsResident = jsonNode.get("permanentSameAsResident");
		Accounts account = objectMapper.convertValue(jsonNode, Accounts.class);
		
		if(!Validator.nonNullFieldsValidator(jsonNode.get("residentAddress"), Address.getNonNullFields())) {
			throw new Exception("Bad residentAddress");
		}
		
		else if(!Validator.nonNullFieldsValidator(jsonNode.get("occupationDetails"), JobDetail.getNonNullFields())) {
			throw new Exception("Bad occupationAddress");
		}
		
		else if(!Validator.dateFormatValidator(jsonNode.get("dob"))) {
			throw new Exception("Bad dob");
		}
		
		else if (sameAsResident !=null && !sameAsResident.isNull() && sameAsResident.asBoolean()) {
			account.setPermanentAddress(account.getResidentAddress());
		}
		
		else {
			if(!Validator.nonNullFieldsValidator(jsonNode.get("permanentAddress"), Address.getNonNullFields())) {
				throw new Exception("Bad PermanentAddress");
			}
		}
		return account;
	}
	// build create account REST API
	@PostMapping()
	public ResponseEntity<Map<String,String>> saveAccounts(@RequestBody JsonNode jsonNode){
		long accNumber;
		Map<String,String>mp = new HashMap<>();
		try {
			if(jsonNode.has("accNumber"))
				throw new Exception("accNumber will be automatically generated");
			Accounts account = validateAccountsData(jsonNode);
			account.setApproved(false);
			accNumber = accountService.saveAccounts(account).getAccNumber();
			mp.put("accNumber", Long.toString(accNumber));
		}
		
		catch(Exception e) {
			mp.put("message", e.getMessage());
			LOGGER.error(e.toString());
			return new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<>(mp, HttpStatus.CREATED);
	}
	
	// build get all accounts REST API
	@GetMapping
	public List<Accounts> getAllAccounts(){
		return accountService.getUnapprovedAccounts();
	}
	
	// build get account by id REST API
	// http://localhost:8080/api/accounts/1
	@GetMapping("{id}")
	public ResponseEntity<Accounts> getAccountsById(@PathVariable("id") long accountId){
		return new ResponseEntity<Accounts>(accountService.getAccountsById(accountId), HttpStatus.OK);
	}
	
	// build update account REST API
	// http://localhost:8080/api/accounts/1
	@PutMapping("{id}")
	public ResponseEntity<Map<String,String>> approveAccounts(@PathVariable("id") long id){
		Map<String, String> mp = new HashMap<>();
		mp.put("accNumber", Long.toString(id));
		try {
			Accounts account = accountService.getAccountsById(id);
			account.setApproved(true);
			accountService.saveAccounts(account);
			mp.put("message", "Account Approved");
		}
		catch (Exception e){
			mp.put("message", "Account Does Not exist");
			new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
		}
			return new ResponseEntity<>(mp, HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<Map<String,String>> updateAccountsByUser(@RequestBody JsonNode jsonNode){
		Map<String, String> mp = new HashMap<>();
		
		try {
			User user = uService.getCurrentUser();
			Accounts prevAccount = user.getAccount();
			Accounts account = validateAccountsData(jsonNode);
			if(account.getAccNumber()!=prevAccount.getAccNumber())
				throw new Exception("Account Number doesn't belong to the user");
			account.setBalance(prevAccount.getBalance());
			accountService.saveAccounts(account);
			mp.put("message", "Account updated");
		}
		catch (Exception e){
			mp.put("message", "Account Does Not exist");
			new ResponseEntity<>(mp, HttpStatus.BAD_REQUEST);
		}
			return new ResponseEntity<>(mp, HttpStatus.OK);
	}
	
	// build delete account REST API
	// http://localhost:8080/api/accounts/1
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteAccountsByAdmin(@PathVariable("id") long id){
		
		// delete account from DB
		try {
			if(accountService.getAccountsById(id).getApproved())
				throw new Exception("Account is Already Approved");
//			uService.deleteUser(uService.findByAccount(id));
			accountService.deleteAccounts(id);
			
			return new ResponseEntity<String>("Accounts deleted successfully!.", HttpStatus.OK);
		}
		catch (Exception e){
			LOGGER.info(e.getMessage());
			return new ResponseEntity<String>("Accounts does not exist or Not permiited to delete!.", HttpStatus.BAD_REQUEST);
		}
	}
	
}