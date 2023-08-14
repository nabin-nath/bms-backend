package com.example.wfbank.model;

//package com.example.demo.model;
import java.math.BigDecimal;
import javax.validation.constraints.Email;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

//import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name="Accounts")
@NoArgsConstructor
@AllArgsConstructor
public class Accounts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="accNumber")
	private long accNumber;

	@Column(name="title", nullable=false)
	private String title;
	
	@Column(name="firstName", nullable=false)
	private String firstName;
	
	@Column(name="lastName", nullable=false)
	private String lastName;
	
	@Column(name="middleName", nullable=true)
	private String middleName;
	
	@Column(name="fatherName", nullable=false)
	private String fatherName;
	
	@Column(name="mobile", nullable=false)
	private String mobile;
	
	@Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	@NotEmpty(message = "Email cannot be empty")
	private String email;
	@Column(name="email", nullable=false)
	private String email;
	
	@Column(name="aadhar", nullable=false)
	private String aadhar;
	
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name="dob", nullable=false)
	private Date dob;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="residentAddress", nullable=false, referencedColumnName= "id")
	private Address residentAddress;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="permanentAddress", nullable=false, referencedColumnName= "id")
	private Address permanentAddress;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="occupationDetails", nullable=false)
	private JobDetail occupationDetails;
	
	@Column(name="debitCardRequired", nullable=false)
	private boolean debitCardRequired;
	
	@Column(name="netBankingRequired", nullable=false)
	private boolean netBankingRequired;
	
	@Column(name="balance", nullable=false)
	private BigDecimal balance;
}
