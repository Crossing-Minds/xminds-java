package com.crossingminds.xminds.api.exception;

public class ServerException extends XmindsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5792513551266930482L;

	public ServerException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}
