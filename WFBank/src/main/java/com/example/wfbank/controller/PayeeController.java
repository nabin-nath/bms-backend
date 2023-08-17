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
import com.example.wfbank.model.Payee;
import com.example.wfbank.model.Payee;
import com.example.wfbank.service.AccountsService;
import com.example.wfbank.service.PayeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/payee")
public class PayeeController {
	@Autowired private PayeeService payeeService;
	@Autowired private AccountsService accountService;
	private ObjectMapper objectMapper;
	
	public PayeeController(PayeeService payeeService) {
		super();
		this.payeeService = payeeService;
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@PostMapping()
	public ResponseEntity<String> savePayee(@RequestBody JsonNode jsonNode) throws JsonMappingException, JsonProcessingException{
//		JsonNode jsonNode = objectMapper.readTree(requestBody);
		long payeeId;
		try {
			Accounts account = accountService.getAccountsById(jsonNode.get("accNumber").asLong());
			Payee payee = objectMapper.treeToValue(jsonNode, Payee.class);
			payee.setAccount(account);
			payeeId = payeeService.savePayee(payee).getPayeeId();
		}
		catch (Exception e) {
			return new ResponseEntity<>("Error Message "+e.getMessage(),HttpStatus.BAD_REQUEST);
		}
			return new ResponseEntity<>("Payee Created Succesfully \nPayee Id is "+payeeId, HttpStatus.CREATED);
	}
	
	// build get all Payee REST API
	@GetMapping
	public List<Payee> getAllPayee(){
		return payeeService.getAllPayee();
	}

	// build get account by id REST API
	// http://localhost:8080/api/Payee/1
	@GetMapping("{id}")
	public ResponseEntity<Payee> getPayeeById(@PathVariable("id") long payeeId){
		return new ResponseEntity<Payee>(payeeService.getPayeeById(payeeId), HttpStatus.OK);
	}
	
	// build update account REST API
		// http://localhost:8080/api/Payee/1
		@PutMapping("{id}")
		public ResponseEntity<Payee> updatePayee(@PathVariable("id") long id
													  ,@RequestBody Payee payee){
			return new ResponseEntity<Payee>(payeeService.updatePayee(payee, id), HttpStatus.OK);
		}
		
		// build delete account REST API
		// http://localhost:8080/api/Payee/1
		@DeleteMapping("{id}")
		public ResponseEntity<String> deletePayee(@PathVariable("id") long id){
			
			// delete account from DB
			payeeService.deletePayee(id);
			
			return new ResponseEntity<String>("Payee deleted successfully!.", HttpStatus.OK);
		}
}
