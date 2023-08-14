package com.example.wfbank.service;
import java.util.List;

//import org.springframework.stereotype.Service;

import com.example.wfbank.model.User;

public interface UserService {
	User saveUser(User User);
	List<User> getAllUser();
	User getUserById(long id);
	User updateUser(User user, long id);
	void deleteUser(long id);
}