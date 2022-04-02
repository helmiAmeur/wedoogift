package com.wedoogift.challenge.exception;

public final class ErrorConstants {

	public static final String USER_NOT_FOUND = "User could not be found!";
	public static final String USER_ACCOUNT_NOT_FOUND = "User don't have account yet!";
	public static final String ACCOUNT_NOT_FOUND = "Account could not be found! ";
	public static final String COMPANY_NOT_FOUND = "Company could not be found! ";
	public static final String REQUIRED_PARAM = "Absent mandatory param(s): accountid,companyId,type or ammount ";
	public static final String BALANCE_EXCEPTION = "the company does not have as much balance to carry out this operation";


	private ErrorConstants() {
	}
}
