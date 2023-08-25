package com.example.wfbank.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="Payee", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"beneficiaryAccNumber", "accNumber"})
})
@NoArgsConstructor
@AllArgsConstructor
public class Payee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	@Column(name="beneficiaryName", nullable=false)
	private String beneficiaryName;
	@Column(name="beneficiaryAccNumber",columnDefinition="bigint not null references accounts(acc_number) on delete cascade")
	private long beneficiaryAccNumber;
	@Column(name="nickname", nullable=true)
	private String nickname;
	
	@Column(name="accNumber", columnDefinition="bigint not null references accounts(acc_number) on delete cascade")
	private long accNumber;
	
}
