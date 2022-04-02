package com.wedoogift.challenge.exception;

public class BalanceExeption extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public BalanceExeption() {
		super(ErrorConstants.BALANCE_EXCEPTION);
	}
}
