package com.example.wfbank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.wfbank.model.Payee;
import com.example.wfbank.model.User;
import com.example.wfbank.service.AccountsService;
import com.example.wfbank.service.PayeeService;
import com.example.wfbank.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/payee")
public class PayeeController {
	@Autowired private PayeeService payeeService;
	@Autowired private AccountsService accountService;
	@Autowired private UserService userService;
	private ObjectMapper objectMapper;
	
	public PayeeController(PayeeService payeeService, UserService userService) {
		super();
		this.payeeService = payeeService;
		this.userService = userService;
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@PostMapping()
	public ResponseEntity<String> savePayee(@RequestBody JsonNode jsonNode) throws JsonMappingException, JsonProcessingException{
//		JsonNode jsonNode = objectMapper.readTree(requestBody);
		User user;
		try {
			user = userService.getCurrentUser();
		}
		catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		}
		long payeeId;
		try {
			if(!accountService.existsById(jsonNode.get("beneficiaryAccNumber").asLong())) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			Payee payee = objectMapper.treeToValue(jsonNode, Payee.class);
			payee.setAccNumber(user.getAccount().getAccNumber());
			payeeId = payeeService.savePayee(payee).getId();
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
	
	@GetMapping("accNumber")
	public ResponseEntity<List<Payee>> getPayeesByAccNumber (){
		long accNumber = userService.getCurrentUser().getAccount().getAccNumber();
		List<Payee> payees = payeeService.getPayeesByAccNumber(accNumber);
		if(payees.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(payees, HttpStatus.OK);
	}

	// build get account by id REST API
	// http://localhost:8080/api/Payee/1
	@GetMapping("accNumber/{id}")
	public ResponseEntity<Payee> getPayeeById(@PathVariable("id") long payeeId){
		try {
			long acc = userService.getCurrentUser().getAccount().getAccNumber();
			long acc2 = payeeService.getPayeeById(payeeId).getAccNumber();
			if(acc!=acc2) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(payeeService.getPayeeById(payeeId), HttpStatus.OK);
	}
	
	// build update account REST API
	// http://localhost:8080/api/Payee/1
	@PutMapping("accNumber/{id}")
	public ResponseEntity<String> updatePayee(@PathVariable("id") long id
												  ,@RequestBody JsonNode jsonNode) throws JsonMappingException, JsonProcessingException{
		try {
			long acc = userService.getCurrentUser().getAccount().getAccNumber();
			if(acc != payeeService.getPayeeById(id).getAccNumber()) {
				return new ResponseEntity<String>("Payee can only be updated by the account holder", HttpStatus.UNAUTHORIZED);
			}
			Payee payee = objectMapper.treeToValue(jsonNode, Payee.class);
			payee.setAccNumber(acc);
			payeeService.updatePayee(payee, id);
		} catch(Exception e) {
			return new ResponseEntity<>("Error Message "+e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Payee with payee id: "+ id +" is updated", HttpStatus.OK);
	}
		
	// build delete account REST API
	// http://localhost:8080/api/Payee/1
	@DeleteMapping("accNumber/{id}")
	public ResponseEntity<String> deletePayee(@PathVariable("id") long id){	
		// delete account from DB
		try {
			long acc = userService.getCurrentUser().getAccount().getAccNumber();
			long acc2 = payeeService.getPayeeById(id).getAccNumber();
			if(acc!=acc2) {
				return new ResponseEntity<>("Only user can delete his payees",HttpStatus.BAD_REQUEST);
			}
		} catch(Exception e){
			return new ResponseEntity<>("Error Message "+e.getMessage(),HttpStatus.BAD_REQUEST);
		}
		try {
			payeeService.deletePayee(id);		
		} catch(Exception e) {
			return new ResponseEntity<>("Error Message"+e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<String>("Payee deleted successfully!.", HttpStatus.OK);
	}
}
