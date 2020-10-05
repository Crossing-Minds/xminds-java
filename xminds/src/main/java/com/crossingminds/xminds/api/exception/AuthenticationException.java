package com.crossingminds.xminds.api.exception;

public class AuthenticationException extends XmindsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4215099874509644985L;

	public AuthenticationException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}
