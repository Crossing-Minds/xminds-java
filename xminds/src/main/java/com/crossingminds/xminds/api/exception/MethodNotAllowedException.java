package com.crossingminds.xminds.api.exception;

public class MethodNotAllowedException extends XmindsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -181640995111005064L;

	public MethodNotAllowedException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}