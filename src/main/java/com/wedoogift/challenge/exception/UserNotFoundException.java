package com.wedoogift.challenge.exception;


public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(Long id) {
		super(ErrorConstants.USER_NOT_FOUND.concat(" with id : " + id));
	}
	public UserNotFoundException() {
		super(ErrorConstants.USER_NOT_FOUND);
	}
}
