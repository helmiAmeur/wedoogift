package com.wedoogift.challenge.exception;

public class RequiredParamException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public RequiredParamException() {
		super(ErrorConstants.REQUIRED_PARAM);
	}
}
