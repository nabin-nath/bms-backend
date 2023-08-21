//package com.example.wfbank.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.wfbank.model.Admins;
//import com.example.wfbank.service.impl.AdminsServiceImpl;
//
//@CrossOrigin("*")
//@RestController
//@RequestMapping("/api/admin")
//public class AdminsController {
//	
//	@Autowired private AdminsServiceImpl adminService;
//	
//	
//	public AdminsController(AdminsServiceImpl adminService) {
//		super();
//		this.adminService = adminService;
//	}
//
//	// build create account REST API
//	@PostMapping()
//	public ResponseEntity<Admins> saveAdmins(@RequestBody Admins admin){
//		return new ResponseEntity<Admins>(adminService.saveAdmin(admin), HttpStatus.CREATED);
//	}
//	
//	// build get all accounts REST API
//	/*
//	@GetMapping
//	public List<Admins> getAllAdmins(){
//		return adminService.getAllAdmins();
//	}*/
//	
//	// build get account by id REST API
//	// http://localhost:8080/api/accounts/1
//	@GetMapping
//	public ResponseEntity<Admins> getAdminById(@PathVariable("id") long adminId){
//		return new ResponseEntity<Admins>(adminService.getAdminById(adminId), HttpStatus.OK);
//	}
//	
//	// build update account REST API
//	// http://localhost:8080/api/accounts/1
//	@PutMapping("{id}")
//	public ResponseEntity<Admins> updateAdmins(@PathVariable("id") long id
//												  ,@RequestBody Admins admin){
//		return new ResponseEntity<Admins>(adminService.updateAdmin(admin, id), HttpStatus.OK);
//	}
//	
//	// build delete account REST API
//	// http://localhost:8080/api/accounts/1
//	@DeleteMapping("{id}")
//	public ResponseEntity<String> deleteAdmins(@PathVariable("id") long id){
//		
//		// delete account from DB
//		adminService.deleteAdmin(id);
//		
//		return new ResponseEntity<String>("Admin deleted successfully!.", HttpStatus.OK);
//	}
//	
//}