package com.crossingminds.xminds.api.exception;

public class XmindsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3101052380589088883L;
	private String msg;
	private String code;
	private String httpStatus;
	private int retryAfter;

	public XmindsException() {
		super();
	}

	public XmindsException(String msg, String code, String httpStatus, int retryAfter) {
		super();
		this.msg = msg;
		this.code = code;
		this.httpStatus = httpStatus;
		this.retryAfter = retryAfter;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(String httpStatus) {
		this.httpStatus = httpStatus;
	}

	public int getRetryAfter() {
		return retryAfter;
	}

	public void setRetryAfter(int retryAfter) {
		this.retryAfter = retryAfter;
	}

}
