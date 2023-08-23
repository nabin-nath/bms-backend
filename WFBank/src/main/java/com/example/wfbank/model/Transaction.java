package com.example.wfbank.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

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
	@GeneratedValue(generator = "randomIdGenerator")
	@GenericGenerator(name = "randomIdGenerator", strategy = "com.example.wfbank.util.RandomIdGenerator" )
	@Column(name="id")
	private long id;
	
	@Column(name="fromAcc", nullable=false)
	private long fromAcc;
	
	@Column(name="toAcc", nullable=false)
	private long toAcc;

	@Column(name="amount", nullable=false)
	private long amount;
	
	@Column(name="transType", nullable=false)
	private String transType;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="IST")
	@Column(name="timeStamp", nullable=false)
	private Date timeStamp;
	
	@Column(name="pin", nullable=false)
	private String pin;
	
//	@ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(referencedColumnName="accNumber")
//	private Accounts toAcc;
//	
//	@ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(referencedColumnName="accNumber")
//	private Accounts fromAcc;
}