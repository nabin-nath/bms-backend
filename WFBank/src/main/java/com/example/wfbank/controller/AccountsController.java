package com.example.wfbank.controller;

import java.util.List;

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
import com.example.wfbank.service.AccountsService;
import com.example.wfbank.util.Validator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
	
	@Autowired private AccountsService accountService;
	private ObjectMapper objectMapper;
	
	public AccountsController(AccountsService accountService) {
		super();
		this.accountService = accountService;
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	// build create account REST API
	@PostMapping()
	public ResponseEntity<String> saveAccounts(@RequestBody JsonNode jsonNode){
		long accNumber;
		try {
			
			JsonNode sameAsResident = jsonNode.get("permanentSameAsResident");
			Accounts account = objectMapper.convertValue(jsonNode, Accounts.class);
			
			if(!Validator.nonNullFieldsValidator(jsonNode.get("residentAddress"), Address.getNonNullFields())) {
				return new ResponseEntity<>("Bad resident Address Data", HttpStatus.BAD_REQUEST);
			}
			
			if(!Validator.nonNullFieldsValidator(jsonNode.get("occupationDetails"), JobDetail.getNonNullFields())) {
				return new ResponseEntity<>("Bad Job Details Data", HttpStatus.BAD_REQUEST);
			}
			
			if(!Validator.dateFormatValidator(jsonNode.get("dob"))) {
				return new ResponseEntity<>("Bad DOB Format", HttpStatus.BAD_REQUEST);
			}
			
			if(!Validator.nonNullFieldsValidator(jsonNode.get("residentAddress"), Address.getNonNullFields())) {
				return new ResponseEntity<>("Bad resident Address Data", HttpStatus.BAD_REQUEST);
			}
			
			if (sameAsResident !=null && !sameAsResident.isNull() && sameAsResident.asBoolean()) {
				account.setPermanentAddress(account.getResidentAddress());
			}
			
			else {
				if(!Validator.nonNullFieldsValidator(jsonNode.get("permanentAddress"), Address.getNonNullFields())) {
					return new ResponseEntity<>("Bad permanent Address Data", HttpStatus.BAD_REQUEST);
				}
			}
			accNumber = accountService.saveAccounts(account).getAccNumber();
		}
		
		catch(Exception e) {
			return new ResponseEntity<>("Bad Form Data\n"+e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		
		
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//		Validator validator = factory.getValidator();
//		Set<ConstraintViolation<Accounts>> violations = validator.validate(account);
////		if (violations.size()!=0) {
//			
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
		return new ResponseEntity<>(String.format("Account Created Succesfully\n Account Number :%d ", accNumber),
				HttpStatus.CREATED);
	}
	
	// build get all accounts REST API
	@GetMapping
	public List<Accounts> getAllAccounts(){
		return accountService.getAllAccounts();
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
	public ResponseEntity<Accounts> updateAccounts(@PathVariable("id") long id
												  ,@RequestBody Accounts account){
		return new ResponseEntity<Accounts>(accountService.updateAccounts(account, id), HttpStatus.OK);
	}
	
	// build delete account REST API
	// http://localhost:8080/api/accounts/1
	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteAccounts(@PathVariable("id") long id){
		
		// delete account from DB
		accountService.deleteAccounts(id);
		
		return new ResponseEntity<String>("Accounts deleted successfully!.", HttpStatus.OK);
	}
	
}