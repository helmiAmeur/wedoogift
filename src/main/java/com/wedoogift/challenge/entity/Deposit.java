package com.wedoogift.challenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DEPOSIT_TYPE",
					 discriminatorType = DiscriminatorType.STRING)
public abstract class Deposit {
	@Id
	@GeneratedValue
	private Long id;
	private Date creationDate;
	private Date expirationDate;
	private boolean expired;
	private double amount;

	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	@JsonIgnoreProperties(value = "deposits",
						  allowSetters = true)
	@ToString.Exclude
	private Account account;

}
