package com.crossingminds.api;

import com.crossingminds.api.exception.AuthenticationException;
import com.crossingminds.api.exception.DuplicatedException;
import com.crossingminds.api.exception.ForbiddenException;
import com.crossingminds.api.exception.JwtTokenExpiredException;
import com.crossingminds.api.exception.MethodNotAllowedException;
import com.crossingminds.api.exception.NotFoundException;
import com.crossingminds.api.exception.RefreshTokenExpiredException;
import com.crossingminds.api.exception.RequestException;
import com.crossingminds.api.exception.ServerException;
import com.crossingminds.api.exception.ServerUnavailableException;
import com.crossingminds.api.exception.TooManyRequestsException;
import com.crossingminds.api.exception.WrongDataException;
import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.BaseError;
import com.crossingminds.api.model.Errors;
import com.crossingminds.api.utils.Constants;

public class Parser {

	private Parser() {
	}

	/**
	 * This method evaluate the response body of all requests to identify handled
	 * errors
	 * 
	 * @param obj
	 * @return the same object
	 * @throws XMindException
	 */
	public static void parseResponse(BaseError baseError) throws XMindException {
		if (baseError != null) {
			String errorName = baseError.getErrorName();
			Errors errorDetails = Errors.valueOf(errorName.toUpperCase());
			var error = baseError.getErrorData().getError();
			var type = baseError.getErrorData().getType();
			var key = baseError.getErrorData().getKey();
			var method = baseError.getErrorData().getMethod();
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
				throw new ServerException(baseError.getMessage() != null ? baseError.getMessage() : errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(),
						0);
			}
		} else {
			throw new ServerException(null, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}
	}

}
