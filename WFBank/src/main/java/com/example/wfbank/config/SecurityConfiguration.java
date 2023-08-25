package com.example.wfbank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.wfbank.filter.JWTAuthenticationFilter;
import com.example.wfbank.filter.JWTAuthorizationFilter;
import com.example.wfbank.service.AuthenticationUserService;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired private final AuthenticationUserService userService;
    @Autowired
	private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private CustomAuthenticationFailureHandler authenticationFailureHandler;
    @Override protected void configure(HttpSecurity http) throws Exception {
    	
        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .cors().and()
        .csrf().disable().authorizeRequests()
        	
            .antMatchers(HttpMethod.POST, AuthenticationConfigConstants.SIGN_UP_URL).permitAll()
            .antMatchers(HttpMethod.POST, "/api/accounts").permitAll()
            .antMatchers("/api/accounts").hasAuthority("ADMIN")
            .antMatchers("/api/accounts/**").hasAuthority("ADMIN")
            .antMatchers("/api/payee","/api/payee/**","/api/user").hasAuthority("USER")
            .antMatchers("/api/user/**").permitAll()
            .antMatchers("/login").permitAll()
           // .antMatchers(null)
            .anyRequest().authenticated()
            .and()
//            .addFilter(new CustomCorsFilter())
            .addFilter(new JWTAuthenticationFilter(authenticationManager(),authenticationFailureHandler,userService))
            .addFilter(new JWTAuthorizationFilter(authenticationManager()))
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()).and()
//        .exceptionHandling().authenticationEntryPoint(new CustomHttp403ForbiddenEntryPoint());

    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
    }

}