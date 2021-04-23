package com.crossingminds.api.exception;

public class NotFoundException extends XMindException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6377383019700745670L;

	public NotFoundException(String msg, String code, int httpStatus, int retryAfter) {
		super(msg, code, httpStatus, retryAfter);
	}

}
