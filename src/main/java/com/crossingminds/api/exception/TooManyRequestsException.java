package com.crossingminds.api.exception;

public class TooManyRequestsException extends XMindException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5389061088773328320L;

	public TooManyRequestsException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}