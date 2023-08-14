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
import com.example.wfbank.service.AccountsService;

@RestController
@RequestMapping("/api/accounts")
public class AccountsController {
	
	@Autowired private AccountsService accountService;

	public AccountsController(AccountsService accountService) {
		super();
		this.accountService = accountService;
	}
	
	// build create account REST API
	@PostMapping()
	public ResponseEntity<Accounts> saveAccounts(@RequestBody Accounts account){
		
		return new ResponseEntity<Accounts>(accountService.saveAccounts(account), HttpStatus.CREATED);
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