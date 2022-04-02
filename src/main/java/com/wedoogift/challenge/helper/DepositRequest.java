package com.wedoogift.challenge.helper;

import lombok.Data;

@Data
public class DepositRequest {
	private Long accountId;
	private Long companyId;
	private Double amount;
	private DepositType type;
}
