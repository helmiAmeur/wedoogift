package com.wedoogift.challenge.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
	@Id
	@GeneratedValue
	private Long id;
	private Date creationDate;
	@OneToMany(
			mappedBy = "account",
			orphanRemoval = true,
			cascade = CascadeType.ALL
	)
	private List<Deposit> deposits = new ArrayList<>();

	public void addDeposit(Deposit deposit) {
		deposits.add(deposit);
		deposit.setAccount(this);
	}
}
