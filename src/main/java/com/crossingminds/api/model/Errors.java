package com.crossingminds.api.model;

public enum Errors {

	// Server Errors
	SERVERERROR("Unknown error from server", "0", 500),
	SERVERUNAVAILABLE("The server is currently unavailable, please try again later", "1", 503),
	TOOMANYREQUESTS("The amount of requests exceeds the limit of your subscription", "2", 429),

	// Authentication Errors
	AUTHERROR("Cannot perform authentication: {error}", "21", 401),
	JWTTOKENEXPIRED("The JWT token has expired", "22", 401),
	REFRESHTOKENEXPIRED("The JWT token has expired", "28", 401),

	// Data Errors
	REQUESTERROR("", "", 400),
	WRONGDATA("There is an error in the submitted data", "40", 400),
	DUPLICATEDERROR("The {type} {key} is duplicated", "42", 400),
	FORBIDDENERROR("Do not have enough permissions to access this resource: {error}", "50", 403),

	// Resource Errors
	NOTFOUNDERROR("The {type} {key} does not exist", "60", 404),
	METHODNOTALLOWED("Method \"{method}\" not allowed", "70", 405);

	private String msg;
	private String code;
	private int httpStatus;

	private Errors(String msg, String code, int httpStatus) {
		this.msg = msg;
		this.code = code;
		this.httpStatus = httpStatus;
	}

	public String getMsg() {
		return msg;
	}

	public String getCode() {
		return code;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

}
