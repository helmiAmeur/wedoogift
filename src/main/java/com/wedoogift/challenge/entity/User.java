package com.wedoogift.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@OneToOne(cascade = CascadeType.ALL)
	private Account account;
	@OneToOne(cascade = CascadeType.ALL)
	private Company Company;

}
