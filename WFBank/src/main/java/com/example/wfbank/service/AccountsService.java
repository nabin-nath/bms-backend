package com.example.wfbank.service;

import java.util.List;

import com.example.wfbank.model.Accounts;

public interface AccountsService {
	Accounts saveAccounts(Accounts Accounts);
	List<Accounts> getAllAccounts();
	Accounts getAccountsById(long id);
	Accounts updateAccounts(Accounts Accounts, long id);
	void deleteAccounts(long id);
}
