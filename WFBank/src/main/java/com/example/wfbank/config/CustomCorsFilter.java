//package com.example.wfbank.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//@Configuration
//public class CustomCorsFilter extends CorsFilter {
//
//    public CustomCorsFilter() {
//        super(configurationSource());
//    }
//    @Bean
//    public static UrlBasedCorsConfigurationSource configurationSource() {
//    	System.out.println("Code is being used");
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowCredentials(true);
//        configuration.addAllowedOriginPatterns("http://localhost:3000");
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.setMaxAge(3600L);
//    
//        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
//        corsConfigurationSource.registerCorsConfiguration("/**", configuration);
//        
//        return corsConfigurationSource;
//    }
//}