package com.crossingminds.xminds.api.exception;

public class JwtTokenExpiredException extends XmindsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6146291403948762890L;

	public JwtTokenExpiredException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}
