package com.example.wfbank.model;

import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="Transaction")
@NoArgsConstructor
@AllArgsConstructor

public class Transaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="Id")
	private long Id;
	
//	@Column(name="fromAccNo", nullable=false)
//	private long fromAccNo;
//	
//	@Column(name="toAccNo", nullable=false)
//	private long toAccNo;
	
	@Column(name="amount", nullable=false)
	private String amount;
	
	@Column(name="transType", nullable=false)
	private String transType;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="IST")
	@Column(name="timeStamp", nullable=false)
	private Date timeStamp;
	
	@Column(name="pin", nullable=false)
	private String pin;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName="accNumber")
	private Accounts toAcc;
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName="accNumber")
	private Accounts fromAcc;
}