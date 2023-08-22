package com.example.wfbank.service;

import java.util.Arrays;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.wfbank.model.Admins;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationUserService implements UserDetailsService {

	private final UserService userService;
	private final AdminsService adminService;
	private static final String ADMIN = "ADMIN";
	private static final String USER = "USER";
	private static final int MAX_FAILED_ATTEMPT = 3;
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Override
	public UserDetails loadUserByUsername(String IdType) throws UsernameNotFoundException {
		// userId:role

		int idx = IdType.lastIndexOf(":");
		String userId = IdType.substring(0, idx), userType = IdType.substring(idx + 1), password;
		boolean nonLocked = false;
		try {

			if (userType.equals(USER)) {

				com.example.wfbank.model.User user = userService.getUserById(Long.parseLong(userId));
				password = user.getPassword();
				nonLocked = user.getFailedAttempts() < MAX_FAILED_ATTEMPT;
			} else if (userType.equals(ADMIN)) {
				Admins admin = adminService.getAdminById(Long.parseLong(userId));
				password = admin.getPassword();
				nonLocked = admin.getFailedAttempts() < MAX_FAILED_ATTEMPT;
			} else {
				throw new BadCredentialsException("Unknown Role");
			}

		} catch (Exception e) {
			LOGGER.error("Username Not Found");
			throw new UsernameNotFoundException(userId);
		}
		return new User(userId, password, true, true, true, nonLocked, getAuthorities(userType));
	}

    public int failedLoginAttempt(String IdType) {
		int idx = IdType.lastIndexOf(":");
		String userId = IdType.substring(0, idx), userType = IdType.substring(idx + 1);
		int val=-1;
		try {

			if (userType.equals(USER)) {

				com.example.wfbank.model.User user = userService.getUserById(Long.parseLong(userId));
				if(user!=null && user.getFailedAttempts()<MAX_FAILED_ATTEMPT)
					user.setFailedAttempts(user.getFailedAttempts()+1);
				val = user.getFailedAttempts();
				userService.saveUser(user);
				
			} 
			else if (userType.equals(ADMIN)) {
				Admins admin = adminService.getAdminById(Long.parseLong(userId));
				if(admin!=null && admin.getFailedAttempts()<MAX_FAILED_ATTEMPT)
					admin.setFailedAttempts(admin.getFailedAttempts()+1);
				val = admin.getFailedAttempts();
				adminService.saveAdmin(admin);
			}
		}
		catch (Exception e){
		}
		return val;
    }

    public void resetLoginAttempt(String IdType) {
		int idx = IdType.lastIndexOf(":");
		String userId = IdType.substring(0, idx), userType = IdType.substring(idx + 1);
		try {

			if (userType.equals(USER)) {

				com.example.wfbank.model.User user = userService.getUserById(Long.parseLong(userId));
				if(user!=null )
					user.setFailedAttempts(0);
				userService.saveUser(user);
			} 
			else if (userType.equals(ADMIN)) {
				Admins admin = adminService.getAdminById(Long.parseLong(userId));
				if(admin!=null)// && admin.getFailedAttempts()<MAX_FAILED_ATTEMPT)
					admin.setFailedAttempts(0);
				adminService.saveAdmin(admin);
			}
		}
		catch (Exception e){
		}
		
    }
    
	private Collection<? extends GrantedAuthority> getAuthorities(String role) {
		return Arrays.asList(new SimpleGrantedAuthority(role));
	}
}