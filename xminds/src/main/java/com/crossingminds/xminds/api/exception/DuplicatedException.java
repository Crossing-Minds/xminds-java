package com.crossingminds.xminds.api.exception;

public class DuplicatedException extends XmindsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8936506798509204781L;

	public DuplicatedException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}
