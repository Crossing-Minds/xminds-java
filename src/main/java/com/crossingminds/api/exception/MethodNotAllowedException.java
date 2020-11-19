package com.crossingminds.api.exception;

public class MethodNotAllowedException extends XMindException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -181640995111005064L;

	public MethodNotAllowedException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}