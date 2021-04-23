package com.crossingminds.api.exception;

public class WrongDataException extends XMindException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -402060068657683688L;

	public WrongDataException(String msg, String code, int httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}