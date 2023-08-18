package com.example.wfbank.service;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationUserService implements UserDetailsService {

    private final UserService userService;
    private final AdminsService adminService;
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";

    @Override public UserDetails loadUserByUsername(String IdType) throws UsernameNotFoundException {
        //userId:role
		
    	String [] arr = IdType.split(":");
		String userId = arr[0], userType = arr[1], password;
		try {
			System.out.println(IdType+userType + userId);
			if (userType.equals(USER)) {
				
				password = userService.getUserById(Long.parseLong(userId)).getPassword();
			}
			else if (userType.equals(ADMIN)) {
				password = adminService.getAdminById(Long.parseLong(userId)).getPassword();
			}
			else {
				throw new Exception("");
			}
			
				
		}
		catch (Exception e) {
			
            throw new UsernameNotFoundException(userId);
        }
        return new org.springframework.security.core.userdetails.User(userId, password, getAuthorities(userType));
    }
    
    
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }
}