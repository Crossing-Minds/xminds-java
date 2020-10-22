package com.crossingminds.api.exception;

public class ServerException extends XMindException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5792513551266930482L;

	public ServerException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

	public ServerException(Throwable throwable, String msg, String code, String httpStatus, int retryAfter) {
		super(throwable, msg, code, httpStatus, retryAfter);
	}

}
