package com.crossingminds.xminds.api.model;

public enum Errors {

	// Server Errors
	ServerError("Unknown error from server", "0", "500"),
	ServerUnavailable("The server is currently unavailable, please try again later", "1", "503"),
	TooManyRequests("The amount of requests exceeds the limit of your subscription", "2", "429"),

	// Authentication Errors
	AuthError("Cannot perform authentication: {error}", "21", "401"),
	JwtTokenExpired("The JWT token has expired", "22", ""),
	RefreshTokenExpired("The JWT token has expired", "28", ""),

	// Authentication Errors
	RequestError("", "", "400"),
	WrongData("There is an error in the submitted data", "40", ""),
	DuplicatedError("The {type} {key} is duplicated", "42", ""),
	ForbiddenError("Do not have enough permissions to access this resource: {error}", "50", "403"),

	// Resource Errors
	NotFoundError("The {type} {key} does not exist", "60", "404"),
	MethodNotAllowed("Method \"{method}\" not allowed", "70", "405");

	private String msg;
	private String code;
	private String httpStatus;

	private Errors(String msg, String code, String httpStatus) {
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

	public String getHttpStatus() {
		return httpStatus;
	}

}
