package com.crossingminds.xminds.api;

import com.crossingminds.xminds.api.exception.AuthenticationException;
import com.crossingminds.xminds.api.exception.DuplicatedException;
import com.crossingminds.xminds.api.exception.ForbiddenException;
import com.crossingminds.xminds.api.exception.JwtTokenExpiredException;
import com.crossingminds.xminds.api.exception.MethodNotAllowedException;
import com.crossingminds.xminds.api.exception.NotFoundException;
import com.crossingminds.xminds.api.exception.RefreshTokenExpiredException;
import com.crossingminds.xminds.api.exception.RequestException;
import com.crossingminds.xminds.api.exception.ServerException;
import com.crossingminds.xminds.api.exception.ServerUnavailableException;
import com.crossingminds.xminds.api.exception.TooManyRequestsException;
import com.crossingminds.xminds.api.exception.WrongDataException;
import com.crossingminds.xminds.api.exception.XmindsException;
import com.crossingminds.xminds.api.mapper.JSONObjectMapper;
import com.crossingminds.xminds.api.model.Base;
import com.crossingminds.xminds.api.model.BaseError;
import com.crossingminds.xminds.api.model.Errors;

public class Parser {

	private Parser() {
	}

	/**
	 * This method evaluate the response body of all requests to identify handled
	 * errors
	 * 
	 * @param obj
	 * @return the same object
	 * @throws XmindsException
	 */
	public static Object parseResponse(Object obj) throws XmindsException {
		Base base = null;
		try {
			base = (Base) obj;
		} catch(ClassCastException ex) {
			JSONObjectMapper<BaseError> baseMapper = new JSONObjectMapper<>();
			base = new Base();
			base.setError(baseMapper.jsonToObject((String) obj, BaseError.class));
		}
		if (base.getError().getErrorName() != null) {
			String errorName = base.getError().getErrorName();
			Errors errorDetails = Errors.valueOf(errorName.toUpperCase());
			var error = base.getError().getErrorData().getError();
			var type = base.getError().getErrorData().getType();
			var key = base.getError().getErrorData().getKey();
			var method = base.getError().getErrorData().getMethod();
			switch (errorName) {
			case "ServerUnavailable":
				throw new ServerUnavailableException(errorDetails.getMsg(), errorDetails.getCode(),
						errorDetails.getHttpStatus(), 0);
			case "TooManyRequests":
				throw new TooManyRequestsException(errorDetails.getMsg(), errorDetails.getCode(),
						errorDetails.getHttpStatus(), 0);
			case "AuthError":
				throw new AuthenticationException(errorDetails.getMsg().replace("{error}", error),
						errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
			case "JwtTokenExpired":
				throw new JwtTokenExpiredException(errorDetails.getMsg(), errorDetails.getCode(),
						errorDetails.getHttpStatus(), 0);
			case "RefreshTokenExpired":
				throw new RefreshTokenExpiredException(errorDetails.getMsg(), errorDetails.getCode(),
						errorDetails.getHttpStatus(), 0);
			case "RequestError":
				throw new RequestException(errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(),
						0);
			case "WrongData":
				throw new WrongDataException(errorDetails.getMsg(), errorDetails.getCode(),
						errorDetails.getHttpStatus(), 0);
			case "DuplicatedError":
				throw new DuplicatedException(errorDetails.getMsg().replace("{type}", type).replace("{key}", key),
						errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
			case "ForbiddenError":
				throw new ForbiddenException(errorDetails.getMsg().replace("{error}", error), errorDetails.getCode(),
						errorDetails.getHttpStatus(), 0);
			case "NotFoundError":
				throw new NotFoundException(errorDetails.getMsg().replace("{type}", type).replace("{key}", key),
						errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
			case "MethodNotAllowed":
				throw new MethodNotAllowedException(errorDetails.getMsg().replace("{method}", method),
						errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
			case "ServerError":
			default:
				throw new ServerException(errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(),
						0);
			}
		}
		return obj;
	}

}
