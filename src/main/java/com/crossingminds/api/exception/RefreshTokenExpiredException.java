package com.crossingminds.api.exception;

public class RefreshTokenExpiredException extends XMindException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7260354452554728520L;

	public RefreshTokenExpiredException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}
