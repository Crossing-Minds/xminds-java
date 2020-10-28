package com.crossingminds.api.exception;

public class XMindException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3101052380589088883L;
	private final String msg;
	private final String code;
	private final String httpStatus;
	private final int retryAfter;

	public XMindException() {
		super();
		this.msg="";
		this.code="";
		this.httpStatus="";
		this.retryAfter=0;
	}

	public XMindException(Throwable throwable, String message) {
		super(message, throwable);
		this.msg="";
		this.code="";
		this.httpStatus="";
		this.retryAfter=0;
	}

	public XMindException(String msg, String code, String httpStatus, int retryAfter) {
		super(msg);
		this.msg = msg;
		this.code = code;
		this.httpStatus = httpStatus;
		this.retryAfter = retryAfter;
	}

	public XMindException(Throwable throwable, String msg, String code, String httpStatus, int retryAfter) {
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

	public String getHttpStatus() {
		return httpStatus;
	}

	public int getRetryAfter() {
		return retryAfter;
	}

}

