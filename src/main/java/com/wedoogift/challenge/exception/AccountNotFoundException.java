package com.wedoogift.challenge.exception;

public class AccountNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AccountNotFoundException(Long id) {
		super(ErrorConstants.USER_ACCOUNT_NOT_FOUND.concat(" with id : " + id));
	}

	public AccountNotFoundException() {
		super(ErrorConstants.ACCOUNT_NOT_FOUND);
	}
}
