package com.example.wfbank.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	private final Logger LOGGER = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);
    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        // Your custom handling logic here
    	LOGGER.info(exception.getClass().getName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set the HTTP status code
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write("{\"message\":\"Authentication failed: " + exception.getMessage()+"\""); // Customize the response message
    }
}