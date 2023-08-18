package com.example.wfbank.service.impl;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.wfbank.model.Admins;
import com.example.wfbank.repository.AdminRepository;
import com.example.wfbank.service.AdminsService;

@Service
public class AdminsServiceImpl implements AdminsService{
	private AdminRepository adminRepository;

	public AdminsServiceImpl(AdminRepository adminRepository) {
		super();
		this.adminRepository = adminRepository;
	}
	public Admins saveAdmin(Admins admin) {
		// TODO Auto-generated method stub
		return adminRepository.save(admin);
	}

	public List<Admins> getAllAdmins() {
		// TODO Auto-generated method stub
		return adminRepository.findAll();
	}

	public Admins getAdminById(long id) {
		// TODO Auto-generated method stub
		return adminRepository.findById(id).orElseThrow();
	}

	public Admins updateAdmin(Admins admin, long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void deleteAdmin(long id) {
		// TODO Auto-generated method stub
		adminRepository.findById(id);
		adminRepository.deleteById(id);
	}
}
