package com.example.wfbank.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
	@Autowired private BCryptPasswordEncoder passwordEncoder;
	private ObjectMapper objectMapper;
	private Logger LOGGER = LoggerFactory.getLogger(getClass())	;
	public TransactionController(TransactionService TransactionService, UserService userService, 
			AccountsService accountService, BCryptPasswordEncoder passwordEncoder) {
		super();
		this.TransactionService = TransactionService;
		this.userService = userService;
		this.accountsService = accountService;
		this.passwordEncoder = passwordEncoder;
		this.objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@PostMapping()
	public ResponseEntity<Map<String,String>> saveTransaction(@RequestBody JsonNode jsonNode) throws JsonMappingException, JsonProcessingException{
//		JsonNode jsonNode = objectMapper.readTree(requestBody);
		User user;
		Map<String,String>mp = new HashMap<>();
		try {
			user = userService.getCurrentUser();
		}
		catch (Exception e) {
			mp.put("message", e.getMessage());
			return new ResponseEntity<>(mp, HttpStatus.UNAUTHORIZED);
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
				throw new Exception("Transaction can be done by your account only");
			}
			LOGGER.info(user.getPin() + ":"+user.getUserId());
			if(!(passwordEncoder.matches(jsonNode.get("pin").asText(), user.getPin()))) {
				throw new Exception("Invalid Pin");
			}
			
			if(accountsService.existsById(toAcc)==false) {
				throw new Exception("Transaction done to an invalid account");
			}
			
			if(type.equals("RTGS")) {
				if((amount.compareTo(BigDecimal.valueOf(200000)) == -1)&& (amount.compareTo(BigDecimal.valueOf(1000000)) == 1)) {
					throw new Exception("Transaction amount should be between 2 Lakhs and 10 Lakhs");
				}
			}
			else if(type.equals("IMPS")) {
				if(amount.compareTo(BigDecimal.valueOf(500000)) == 1) {
					throw new Exception("Transaction amount should be less than 5 Lakhs");
				}
			}
			else {
				if(amount.compareTo(BigDecimal.valueOf(1000000)) == 1) {
					throw new Exception("Transaction amount should be less than 10 Lakhs");
				}
			}
			if(amount.compareTo(balance) == 1) {
				throw new Exception("Insufficent balance");
			}
			
			BigDecimal toBalance = toAccount.getBalance();
			balance = balance.subtract(amount);
			toBalance = toBalance.add(amount);
			fromAccount.setBalance(balance);
			toAccount.setBalance(toBalance);
			
			accountsService.saveAccounts(fromAccount);
			accountsService.saveAccounts(toAccount);
			
//			accountsService.updateAccounts(fromAccount, fromAcc);
//			accountsService.updateAccounts(toAccount, toAcc);
			
			
			Transaction transaction = objectMapper.treeToValue(jsonNode, Transaction.class);
//			transaction.setFromAcc(transType);
//			transaction.setFromAcc(balance));
//			transaction.setFromAcc(timeStamp);
			transaction.setFromAcc(fromAcc);
			transaction.setToAcc(toAcc);
			transId = TransactionService.saveTransaction(transaction).getId();
		}
		catch (Exception e) {
			mp.put("message", e.getMessage());
			return new ResponseEntity<>(mp,HttpStatus.BAD_REQUEST);
		}
			mp.put("message", "Transaction Succesful");
			mp.put("transId", Long.toString(transId));
			return new ResponseEntity<>(mp, HttpStatus.CREATED);
	}
	
	@GetMapping
	public List<Transaction> getAllTransaction(){
		return TransactionService.getAllTransaction();
	}
	
	@GetMapping("accNumber")
	public ResponseEntity<List<Transaction>> getUsersTransaction() {
		long accNumber = userService.getCurrentUser().getAccount().getAccNumber();
		List<Transaction> transaction = TransactionService.findByUserId(accNumber);
		if(transaction.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(transaction, HttpStatus.OK);
	}
	
	@PostMapping("accNumber/timestamp")
	public ResponseEntity<Page<Transaction>> getTransactionsBetween(@RequestBody JsonNode jsonNode) throws JsonMappingException, JsonProcessingException {
		try {
			int size=2,page=0;
			String type="";
			if(jsonNode.has("page"))
				page = jsonNode.get("page").asInt();
			if(jsonNode.has("size"))
				size = jsonNode.get("size").asInt();
			if(jsonNode.has("type"))
				type = jsonNode.get("type").asText();
			LOGGER.info("Type:"+type);
			Pageable pageable = PageRequest.of(page, size,Sort.by("timeStamp"));
			Long accNo = userService.getCurrentUser().getAccount().getAccNumber();
			Date startTime = objectMapper.convertValue(jsonNode.get("startTime"), Date.class);
			Date endTime = objectMapper.convertValue(jsonNode.get("endTime"), Date.class);
			return new ResponseEntity<>(TransactionService.getBetweenTimeStamp(accNo, startTime, endTime,type, pageable),HttpStatus.OK);
		} catch(Exception e) {
			LOGGER.error(e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
