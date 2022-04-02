package com.wedoogift.challenge.exception;

public class CompanyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CompanyNotFoundException(Long id) {
		super(ErrorConstants.COMPANY_NOT_FOUND.concat("company id : " + id));
	}
	public CompanyNotFoundException() {
		super(ErrorConstants.COMPANY_NOT_FOUND);
	}
}
