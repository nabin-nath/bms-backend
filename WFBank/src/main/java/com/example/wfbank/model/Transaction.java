package com.example.wfbank.model;

//import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
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
	
	@Column(name="fromAcc", columnDefinition="bigint not null references accounts(acc_number) on delete cascade")
	private long fromAcc;
	
	@Column(name="toAcc", columnDefinition="bigint not null references accounts(acc_number)")
	private long toAcc;

	@Column(name="amount", nullable=false)
	private long amount;
	
	@Column(name="transType", nullable=false)
	private String transType;
	
	@CreationTimestamp
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="IST")
	@Column(name="timeStamp", nullable=false)
	private Timestamp timeStamp;
//	@ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(referencedColumnName="accNumber")
//	private Accounts toAcc;
//	
//	@ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(referencedColumnName="accNumber")
//	private Accounts fromAcc;
}