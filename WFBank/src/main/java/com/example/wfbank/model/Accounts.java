package com.example.wfbank.model;

//package com.example.demo.model;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.GenericGenerator;

//import com.example.wfbank.util.RandomIdGenerator;
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
	
	@GeneratedValue(generator = "randomIdGenerator")
	@GenericGenerator(name = "randomIdGenerator", strategy = "com.example.wfbank.util.RandomIdGenerator" )
	@Column(name="accNumber")
	private long accNumber;

	@Column(name="title", nullable=false)
	@NotEmpty
	private String title;
	
	@Column(name="firstName", nullable=false)
	@NotEmpty
	private String firstName;
	
	@Column(name="lastName", nullable=false)
	@NotEmpty
	private String lastName;
	
	@Column(name="middleName", nullable=true)
	private String middleName;
	
	@Column(name="fatherName", nullable=false)
	@NotEmpty
	private String fatherName;
	
	@Column(name="mobile", nullable=false)
	@NotEmpty
	private String mobile;
	
	@Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
	@NotEmpty(message = "Email cannot be empty")
	@Column(name="email", nullable=false)
	private String email;
	
	@Column(name="aadhar", nullable=false)
	@NotEmpty
	private String aadhar;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="IST")
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
	private boolean debitCardRequired=false;
	
	@Column(name="netBankingRequired", nullable=false)
	private boolean netBankingRequired=false;
	
	@Column(name="balance", nullable=false)
	private BigDecimal balance;
	
	private Boolean approved = false;
	
//	@OneToOne(mappedBy = "account" )
//	private User netBank;
}
