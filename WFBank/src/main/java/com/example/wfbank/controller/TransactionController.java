package com.example.wfbank.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wfbank.model.Accounts;
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
			
			Accounts toAccount = accountsService.getAccountsById(toAcc);
			Accounts fromAccount = user.getAccount();
			BigDecimal amount = BigDecimal.valueOf(jsonNode.get("amount").asLong());
			BigDecimal balance = fromAccount.getBalance();
			String type = jsonNode.get("transType").toString();
			
			if(fromAcc != user.getAccount().getAccNumber()) {
				return new ResponseEntity<String>("Transaction can be done by your account only", HttpStatus.BAD_REQUEST);
			}
			if(accountsService.existsById(toAcc)==false) {
				return new ResponseEntity<String>("Transaction done to an invalid account", HttpStatus.BAD_REQUEST);
			}
			
			if(type.equals("RTGS")) {
				if((amount.compareTo(BigDecimal.valueOf(200000)) == -1)&& (amount.compareTo(BigDecimal.valueOf(1000000)) == 1)) {
					return new ResponseEntity<String>("Transaction amount should be between 2 Lakhs and 10 Lakhs", HttpStatus.BAD_REQUEST);
				}
			}
			else if(type.equals("IMPS")) {
				if(amount.compareTo(BigDecimal.valueOf(500000)) == 1) {
					return new ResponseEntity<String>("Transaction amount should be less than 5 Lakhs", HttpStatus.BAD_REQUEST);
				}
			}
			else {
				if(amount.compareTo(BigDecimal.valueOf(1000000)) == 1) {
					return new ResponseEntity<String>("Transaction amount should be less than 10 Lakhs", HttpStatus.BAD_REQUEST);
				}
			}
			if(amount.compareTo(balance) == 1) {
				return new ResponseEntity<String>("Insufficent balance", HttpStatus.BAD_REQUEST);
			}
			
			BigDecimal toBalance = toAccount.getBalance();
			balance = balance.subtract(amount);
			toBalance = toBalance.add(amount);
			fromAccount.setBalance(toBalance);
			toAccount.setBalance(balance);
			accountsService.updateAccounts(fromAccount, fromAcc);
			accountsService.updateAccounts(toAccount, toAcc);
			
			
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
