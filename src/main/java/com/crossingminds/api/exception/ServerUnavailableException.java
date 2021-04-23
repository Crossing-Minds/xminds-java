package com.crossingminds.api.exception;

public class ServerUnavailableException extends XMindException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1613643973500739933L;

	public ServerUnavailableException(String msg, String code, int httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}
