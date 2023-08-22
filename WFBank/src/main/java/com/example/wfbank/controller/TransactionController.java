package com.example.wfbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wfbank.model.Transaction;
import com.example.wfbank.model.User;
import com.example.wfbank.service.AccountsService;
import com.example.wfbank.service.TransactionService;
import com.example.wfbank.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
	@Autowired private TransactionService TransactionService;
	@Autowired private AccountsService accountsService;
	@Autowired private UserService userService;
	private ObjectMapper objectMapper;
	
	public TransactionController(TransactionService TransactionService, UserService userService, AccountsService accountService) {
		super();
		this.TransactionService = TransactionService;
		this.userService = userService;
		this.accountsService = accountService;
		this.objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@PostMapping()
	public ResponseEntity<String> saveTransaction(@RequestBody JsonNode jsonNode) throws JsonMappingException, JsonProcessingException{
//		JsonNode jsonNode = objectMapper.readTree(requestBody);
		User user;
		try {
			user = userService.getCurrentUser();
		}
		catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		long transId;
		try {
			long fromAcc = jsonNode.get("fromAcc").asLong();
			long toAcc = jsonNode.get("toAcc").asLong();
			if(fromAcc != user.getAccount().getAccNumber()) {
				return new ResponseEntity<String>("Transaction can be done by your account only", HttpStatus.BAD_REQUEST);
			}
			if(accountsService.existsById(toAcc)==false) {
				return new ResponseEntity<String>("Transaction done to an invalid account", HttpStatus.BAD_REQUEST);
			}
			Transaction transaction = objectMapper.treeToValue(jsonNode, Transaction.class);
			transaction.setFromAcc(fromAcc);
			transaction.setToAcc(toAcc);
			transId = TransactionService.saveTransaction(transaction).getId();
		}
		catch (Exception e) {
			return new ResponseEntity<>("Error Message "+e.getMessage(),HttpStatus.BAD_REQUEST);
		}
			return new ResponseEntity<>("Transaction Success \nTransaction Id is "+transId, HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<Transaction> getAllTransaction(){
		return TransactionService.getAllTransaction();
	}
}
