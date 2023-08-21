package com.example.wfbank.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.wfbank.config.AuthenticationConfigConstants;
//import com.example.wfbank.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//import lombok.RequiredArgsConstructor;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    public JWTAuthenticationFilter(AuthenticationManager authManager){
    	super();
    	this.authenticationManager = authManager;
    	super.setUsernameParameter("userId");
    }
    
    @Override public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
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
        }
        catch (Exception e) {
        	response.setContentType("application/json");
        	response.setCharacterEncoding("UTF-8");
        	try {
        		response.getWriter().write("{\"message\":\""+e.getMessage()+"\"}");
        		throw new RuntimeException(e);
        	} catch(IOException et) {
        		throw new RuntimeException (et);
        	}
        	
        }
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