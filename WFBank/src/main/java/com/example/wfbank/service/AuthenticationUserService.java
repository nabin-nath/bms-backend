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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationUserService implements UserDetailsService {

    private final UserService userService;
    private final AdminsService adminService;
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Override public UserDetails loadUserByUsername(String IdType) throws UsernameNotFoundException {
        //userId:role
		
    	int idx = IdType.lastIndexOf(":");
		String userId = IdType.substring(0,idx), userType = IdType.substring(idx+1), password;
		try {
			
			if (userType.equals(USER)) {
				
				password = userService.getUserById(Long.parseLong(userId)).getPassword();
			}
			else if (userType.equals(ADMIN)) {
				password = adminService.getAdminById(Long.parseLong(userId)).getPassword();
			}
			else {
				throw new BadCredentialsException("Unknown Role");
			}
			
				
		}
		catch (Exception e) {
			LOGGER.error("Username Not Found");
            throw new UsernameNotFoundException(userId);
        }
        return new User(userId, password, getAuthorities(userType));
    }
    
    
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }
}