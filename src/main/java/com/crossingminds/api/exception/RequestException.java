package com.crossingminds.api.exception;

public class RequestException extends XMindException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3869203146406541106L;

	public RequestException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}
