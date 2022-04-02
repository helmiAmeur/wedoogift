package com.wedoogift.challenge.service;

/**
 * Service Interface for managing {@link com.wedoogift.challenge.entity.User}.
 */
public interface UserService {

	/**
	 * get user's balance.
	 *
	 * @param userId the user's id.
	 * @return the user's balance.
	 */
	double getUserBalance(Long userId);
}
