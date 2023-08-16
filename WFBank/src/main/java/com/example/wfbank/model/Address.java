package com.example.wfbank.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NonNull
	private String line1;
	
	@NotNull
	private String line2;
	
	private String landmark;
	
	@NotNull
	private String city;
	
	@NotNull
	private int pinCode;
	
	@Transient
	static private String  [] nonNullFields = {"line1", "line2", "city", "pinCode"};
	
	static public String [] getNonNullFields() {
		return nonNullFields;
	}
}
