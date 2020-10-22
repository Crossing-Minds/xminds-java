package com.crossingminds.api.exception;

import com.crossingminds.api.model.BaseError;
import com.crossingminds.api.model.ErrorData;
import com.crossingminds.api.utils.Constants;

public class XMindRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4913903927138626613L;
	private BaseError baseError = new BaseError(500, "ServerError", Constants.UNKNOWN_ERROR_MSG, new ErrorData(), null);

	public XMindRuntimeException() {
		super();
	}

	public XMindRuntimeException(Throwable cause) {
		super(cause);
	}

	public XMindRuntimeException(BaseError baseError) {
		super();
		this.baseError = baseError;
	}

	public BaseError getBaseError() {
		return this.baseError;
	}

}
