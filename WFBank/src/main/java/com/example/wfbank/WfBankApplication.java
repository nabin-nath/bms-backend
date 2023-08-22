package com.example.wfbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan
@SpringBootApplication
public class WfBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(WfBankApplication.class, args);
	}

}
