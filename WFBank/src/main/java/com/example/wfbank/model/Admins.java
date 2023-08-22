package com.example.wfbank.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name="Admins")
@NoArgsConstructor
@AllArgsConstructor

public class Admins {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	@Column(name="adminName", nullable=false)
	private String adminName;
	@Column(name="password", nullable=false)
	private String password;
	private int failedAttempts=0;
}
