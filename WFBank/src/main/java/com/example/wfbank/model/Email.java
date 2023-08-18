package com.example.wfbank.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class Email {
	
	private String body;
	private List<String> recipients;
	private String subject;
	
}
