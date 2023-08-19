package com.example.wfbank.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Service;

import com.example.wfbank.model.Email;
import com.example.wfbank.service.EmailService;
import com.example.wfbank.util.OtpGenerator;

@Description(value = "Service responsible for handling OTP related functionality.")
@Service
public class OtpService {

    private final Logger LOGGER = LoggerFactory.getLogger(OtpService.class);

    private OtpGenerator otpGenerator;
    private EmailService emailService;

    /**
     * Constructor dependency injector
     * @param otpGenerator - otpGenerator dependency
     * @param emailService - email service dependency
     * @param userService - user service dependency
     */
    public OtpService(OtpGenerator otpGenerator, EmailService emailService)
    {
        this.otpGenerator = otpGenerator;
        this.emailService = emailService;
        
    }

    /**
     * Method for generate OTP number
     *
     * @param userEmail - email of User
     * @param user - provided key (accNumber in this case)
     * @return boolean value (true|false)
     */
    public Boolean generateOtp(String userEmail, String user)
    {
        // generate otp
        Integer otpValue = otpGenerator.generateOTP(user);
        if (otpValue == -1)
        {
            LOGGER.error("OTP generator is not working...");
            return  false;
        }

        LOGGER.info("Generated OTP: {}", otpValue);

        // fetch user e-mail from database
        
        List<String> recipients = new ArrayList<>();
        recipients.add(userEmail);
        String type = user.substring(user.lastIndexOf(":")+1);
        // generate email object
        Email email = new Email();
        email.setSubject("Spring Boot OTP Password.");
        email.setBody(String.format("OTP Password for %s: %d",type, otpValue));
        email.setRecipients(recipients);

        // send generated e-mail
        return emailService.sendSimpleMessage(email);
    }

    /**
     * Method for validating provided OTP
     *
     * @param key - provided key
     * @param otpNumber - provided OTP number
     * @return boolean value (true|false)
     */
    public Boolean validateOTP(String key, Integer otpNumber)
    {
        // get OTP from cache
        Integer cacheOTP = otpGenerator.getOPTByKey(key);
        if (cacheOTP!=null && cacheOTP.equals(otpNumber))
        {
            otpGenerator.clearOTPFromCache(key);
            return true;
        }
        return false;
    }
}