package com.crossingminds.xminds.api;

import java.util.Map;

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
import com.crossingminds.xminds.api.model.Base;
import com.crossingminds.xminds.api.model.Errors;

public class Parser {

	/**
	 * This method evaluate the response body of all requests
	 * to identify handled errors
	 * 
	 * @param obj
	 * @return the same object
	 * @throws XmindsException
	 */
	public static Object parseResponse(Object obj) throws XmindsException {
		Base base = (Base) obj;
		if (base.getAdditionalProperties().containsKey("error_code")) {
			String errorName = (String) base.getAdditionalProperties().get("error_name");
			Errors errorDetails = Errors.valueOf(errorName);
			Map<String, Object> errorData;
			String error = "";
			String type = "";
			String key = "";
			switch (errorName) {
				case "ServerError":
					throw new ServerException(errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "ServerUnavailable":
					throw new ServerUnavailableException(errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "TooManyRequests":
					throw new TooManyRequestsException(errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "AuthError":
					errorData = (Map<String, Object>) base.getAdditionalProperties().get("error_data");
					error = (String) errorData.get("error");
					throw new AuthenticationException(errorDetails.getMsg().replace("{error}", error), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "JwtTokenExpired":
					throw new JwtTokenExpiredException(errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "RefreshTokenExpired":
					throw new RefreshTokenExpiredException(errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "RequestError":
					throw new RequestException(errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "WrongData":
					throw new WrongDataException(errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "DuplicatedError":
					errorData = (Map<String, Object>) base.getAdditionalProperties().get("error_data");
					type = (String) errorData.get("type");
					key = (String) errorData.get("key");
					throw new DuplicatedException(errorDetails.getMsg().replace("{type}", type).replace("{key", key), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "ForbiddenError":
					errorData = (Map<String, Object>) base.getAdditionalProperties().get("error_data");
					error = (String) errorData.get("error");
					throw new ForbiddenException(errorDetails.getMsg().replace("{error}", error), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "NotFoundError":
					errorData = (Map<String, Object>) base.getAdditionalProperties().get("error_data");
					type = (String) errorData.get("type");
					key = (String) errorData.get("key");
					throw new NotFoundException(errorDetails.getMsg().replace("{type}", type).replace("{key", key), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				case "MethodNotAllowed":
					errorData = (Map<String, Object>) base.getAdditionalProperties().get("error_data");
					String method = (String) errorData.get("method");
					throw new MethodNotAllowedException(errorDetails.getMsg().replace("{method}", method), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
				default:
					throw new ServerException(errorDetails.getMsg(), errorDetails.getCode(), errorDetails.getHttpStatus(), 0);
			}
		}
		return obj;
	}

}
