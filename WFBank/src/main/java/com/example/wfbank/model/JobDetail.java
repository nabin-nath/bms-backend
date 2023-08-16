package com.example.wfbank.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class JobDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	private String occupationType;
	
	@NotNull
	private String sourceOfIncome;
	
	@NotNull
	private BigDecimal grossAnnualIncome;

	@Transient
	private static String [] nonNullFields = {"occupationType", "sourceOfIncome", "grossAnnualIncome"};
	
	public static String [] getNonNullFields() {
		return nonNullFields;
	}
}
