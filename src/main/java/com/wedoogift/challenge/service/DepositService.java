package com.wedoogift.challenge.service;

import com.wedoogift.challenge.helper.DepositRequest;

/**
 * Service Interface for managing {@link com.wedoogift.challenge.entity.Deposit}.
 */
public interface DepositService {

	/**
	 * distribute Deposit from company to user's account.
	 *
	 * @param depositRequest the request.
	 */
	void distributeDeposit(DepositRequest depositRequest);
}
