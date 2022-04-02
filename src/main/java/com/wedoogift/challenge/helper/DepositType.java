package com.wedoogift.challenge.helper;

public enum DepositType {
	GIFT("GIFT"),
	MEAL("MEAL");

	private final String type;

	DepositType(String text) {
		this.type = text;
	}

	@Override
	public String toString() {
		return this.type;
	}
}
