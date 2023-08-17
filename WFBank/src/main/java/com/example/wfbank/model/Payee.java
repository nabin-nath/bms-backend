package com.example.wfbank.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="Payee")
@NoArgsConstructor
@AllArgsConstructor
public class Payee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	@Column(name="beneficiaryName", nullable=false)
	private String beneficiaryName;
	@Column(name="beneficiaryAccNumber", nullable=false)
	private long beneficiaryAccNumber;
	@Column(name="nickname", nullable=true)
	private String nickname;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accNumber", referencedColumnName="accNumber")
	private Accounts account;
	
	public long getPayeeId() {
		return id;
	}
}
