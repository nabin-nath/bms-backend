package com.example.wfbank.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="Users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
	@Id
	@GeneratedValue(generator = "randomIdGenerator")
	@GenericGenerator(name = "randomIdGenerator", strategy = "com.example.wfbank.util.RandomIdGenerator" )
	@Column(name="userId")
	private long userId;

	@Column(name="username", nullable=false)
	private String username;
	
	@Column(name="password", nullable=false)
	private String password;
	
	@Column(name="pin", nullable=false)
	private String pin;
	
	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToOne
    @JoinColumn(name = "accNumber",nullable=false, 
    	referencedColumnName="accNumber", unique=true)
	private Accounts account;
	
	private int failedAttempts = 0;
	

}
