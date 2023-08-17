package com.example.wfbank.service;

import java.util.List;

import com.example.wfbank.model.Accounts;

import java.util.List;
import com.example.wfbank.model.Admins;

public interface AdminsService {
	Admins saveAdmin(Admins admin);
	List<Admins> getAllAdmins();
	Admins getAdminById(long id);
	Admins updateAdmin(Admins admin, long id);
	void deleteAdmin(long id);
}
