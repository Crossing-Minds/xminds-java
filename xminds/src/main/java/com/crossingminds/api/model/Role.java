package com.crossingminds.api.model;

public enum Role {

	ROOT("root"),
	MANAGER("manager"),
	BACKEND("backend"),
	FRONTEND("frontend");

	private String value;

	Role(String value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

}
