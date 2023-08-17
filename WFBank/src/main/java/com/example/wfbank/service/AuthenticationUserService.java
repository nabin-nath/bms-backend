package com.example.wfbank.service;

import com.example.wfbank.model.User;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationUserService implements UserDetailsService {

    private final UserService userService;

    @Override public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userService.getUserById(Long.parseLong(userId));
        if (user == null) {
            throw new UsernameNotFoundException(userId);
        }
        return new org.springframework.security.core.userdetails.User(Long.toString(user.getUserId()), user.getPassword(), Collections.emptyList());
    }
}