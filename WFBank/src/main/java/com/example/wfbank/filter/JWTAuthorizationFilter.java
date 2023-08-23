package com.example.wfbank.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
//import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.wfbank.config.AuthenticationConfigConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
	private final Logger LOGGER = LoggerFactory.getLogger(JWTAuthorizationFilter.class);
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    	
    	String header = request.getHeader(AuthenticationConfigConstants.HEADER_STRING);
    	
        if (header == null || !header.startsWith(AuthenticationConfigConstants.TOKEN_PREFIX)) {
        	
            chain.doFilter(request, response);
            return;
        }
        try {
        	UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        	SecurityContextHolder.getContext().setAuthentication(authentication);
            
        }
        catch (TokenExpiredException e) {
        	response.setHeader("error", "session expired");
//        	throw new BadCredentialsException(e.getMessage());
    	}
        catch (Exception exception){
            LOGGER.error("Error logging in : {} ", exception.getMessage());
//            response.setHeader("error",exception.getMessage());
//            response.setStatus(HttpStatus.UNAUTHORIZED.value());
//            Map<String,String > error = new HashMap<>();
//            error.put("message",exception.getMessage());
//            response.setContentType("application/json");
//            new ObjectMapper().writeValue(response.getOutputStream(),error);
////            assert();
//            throw new BadCredentialsException(exception.getMessage());
        }
        
        chain.doFilter(request, response);
    }
    
      private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    	
        String token = request.getHeader(AuthenticationConfigConstants.HEADER_STRING);
        
        if (token != null) {
            // parse the token.
          
            DecodedJWT verify = JWT.require(Algorithm.HMAC512(AuthenticationConfigConstants.SECRET.getBytes()))
                    .build()
                    .verify(token.replace(AuthenticationConfigConstants.TOKEN_PREFIX, ""));

            String username = verify.getSubject();
            String role = verify.getClaim("role").asString();
            
            if (username != null) {
                return new UsernamePasswordAuthenticationToken(username, null, getAuthorities(role));
            }
            return null;
        }
        return null;
    }
    
    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Arrays.asList(new SimpleGrantedAuthority(role));
    }

}