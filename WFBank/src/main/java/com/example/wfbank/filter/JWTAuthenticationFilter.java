package com.example.wfbank.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.wfbank.config.AuthenticationConfigConstants;
import com.example.wfbank.config.CustomAuthenticationFailureHandler;
import com.example.wfbank.service.AuthenticationUserService;
//import com.example.wfbank.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//import lombok.RequiredArgsConstructor;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    private final AuthenticationManager authenticationManager;
    private final AuthenticationUserService userService;
    @Autowired
    public JWTAuthenticationFilter(AuthenticationManager authManager, CustomAuthenticationFailureHandler failureHandler,
    		AuthenticationUserService userService){
    	super();
    	setAuthenticationFailureHandler(failureHandler);
    	this.authenticationManager = authManager;
    	this.userService = userService;
    	super.setUsernameParameter("userId");
    }
    
    @Override public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) 
    					throws AuthenticationException {
        try {
        	
            JsonNode creds = new ObjectMapper()
                .readTree(request.getInputStream());
            
            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    creds.get("userId").asText()+":"+creds.get("role").asText(),
                    creds.get("password").asText(),
                    new ArrayList<>())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
        	throw new AuthenticationCredentialsNotFoundException("userId, password or role not provided");
        }
        
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
    	super.unsuccessfulAuthentication(request, response, failed);
    	String idType;
    	int val=-2;
    	if(failed instanceof BadCredentialsException) {
    		try {
	    		JsonNode creds = new ObjectMapper()
	                .readTree(request.getInputStream());
	            idType = creds.get("userId").asText()+":"+creds.get("role").asText();
	            val = userService.failedLoginAttempt(idType);
	    	}	    	
	    	catch (Exception e) {
	    	}
    	}
    	if(val>=0) {
    		response.getWriter().write(",\"failedAttempts\":"+Integer.toString(val));
    	}
    	response.getWriter().write("\"}");
    }
    	

    @Override protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
    	
    	String token = JWT.create()
            .withSubject(((User) auth.getPrincipal()).getUsername())
            .withClaim("role", auth.getAuthorities().iterator().next().getAuthority())
            .withExpiresAt(new Date(System.currentTimeMillis() + AuthenticationConfigConstants.EXPIRATION_TIME))
            .sign(Algorithm.HMAC512(AuthenticationConfigConstants.SECRET.getBytes()));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
            "{\"" + AuthenticationConfigConstants.HEADER_STRING + "\":\"" + AuthenticationConfigConstants.TOKEN_PREFIX + token + "\"}"
        );
    }
}