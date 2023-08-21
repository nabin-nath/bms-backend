package com.example.wfbank.service;

import com.example.wfbank.model.Email;

public interface EmailService {
	boolean sendSimpleMessage(Email mail);
}
