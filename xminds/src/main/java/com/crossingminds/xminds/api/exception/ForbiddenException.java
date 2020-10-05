package com.crossingminds.xminds.api.exception;

public class ForbiddenException extends XmindsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8933259466364897601L;

	public ForbiddenException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}