package com.crossingminds.api.exception;

import java.io.IOException;

public class XMindException extends IOException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3101052380589088883L;
	private final String msg;
	private final String code;
	private final int httpStatus;
	private final int retryAfter;

	public XMindException() {
		super();
		this.msg="";
		this.code="";
		this.httpStatus=0;
		this.retryAfter=0;
	}

	public XMindException(Throwable throwable, String message) {
		super(message, throwable);
		this.msg="";
		this.code="";
		this.httpStatus=0;
		this.retryAfter=0;
	}

	public XMindException(String msg, String code, int httpStatus, int retryAfter) {
		super(msg);
		this.msg = msg;
		this.code = code;
		this.httpStatus = httpStatus;
		this.retryAfter = retryAfter;
	}

	public XMindException(Throwable throwable, String msg, String code, int httpStatus, int retryAfter) {
		super(msg, throwable);
		this.msg = msg;
		this.code = code;
		this.httpStatus = httpStatus;
		this.retryAfter = retryAfter;
	}

	public String getMsg() {
		return msg;
	}

	public String getCode() {
		return code;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public int getRetryAfter() {
		return retryAfter;
	}

}

