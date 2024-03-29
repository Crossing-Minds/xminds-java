package com.crossingminds.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import com.crossingminds.api.exception.ServerException;
import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.BaseError;
import com.crossingminds.api.model.Token;
import com.crossingminds.api.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * This class implements the low level request logic of Crossing Minds API
 *
 */
public class Request {

	/*
	 * JSON ObjectMapper
	 */
	private static final ObjectMapper MAPPER = new ObjectMapper();
	/*
	 * Array of Headers to use in all requests
	 */
	private static String[] HEADERS = getHeaders();

	// INSTANCE ATTRIBUTES
	/*
	 * Contains data related to security
	 */
	private Token token = new Token();
	/*
	 * Contains the host of the API
	 */
	private String host = "";
	/*
	 * Contains the external user agent
	 */
	private String userAgent = "";
	/*
	 * HttpClient
	 */
	private HttpClient httpClient = null;

	/**
	 * Default constructor
	 */
	protected Request() {
		this(HttpClient.newHttpClient(), Constants.API_URL, "");
	}

	/**
	 * Constructor
	 * 
	 * @param httpClient
	 * @param host
	 * @param userAgent
	 */
	protected Request(HttpClient httpClient, String host, String userAgent) {
		this.httpClient = httpClient != null ? httpClient : HttpClient.newHttpClient();
		this.host = StringUtils.isNotBlank(host) ? host : Constants.API_URL;
		this.userAgent = StringUtils.isNotBlank(userAgent) ? userAgent : "";
		updateUserAgentHeader();
	}

	private static String[] getHeaders() {
		var headers = new String[8];
		headers[0] = Constants.HEADER_USER_AGENT;
		headers[1] = Constants.HEADER_USER_AGENT_VALUE;
		headers[2] = Constants.HEADER_CONTENT_TYPE;
		headers[3] = Constants.HEADER_CONTENT_TYPE_JSON_VALUE;
		headers[4] = Constants.HEADER_ACCEPT;
		headers[5] = Constants.HEADER_ACCEPT_JSON_VALUE;
		headers[6] = Constants.HEADER_AUTHORIZATION;
		headers[7] = Constants.HEADER_AUTHORIZATION_VALUE;
		return headers;
	}
	private <T> T readValue(String content, Class<T> valueType) throws JsonProcessingException {
		return MAPPER.readValue(content.isBlank() ? "{}" : content, valueType);
	}

	private String writeValueAsString(Object obj) {
		try {
			return MAPPER.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public <T> T get(String endpoint, String queryParams, Class<T> valueType) throws XMindException {
		return sendHttpRequest(
				getHttpRequestBuilder(getURI(endpoint)).method("GET", BodyPublishers.ofString(queryParams)).build(),
				valueType);
	}

	public <T> T get(String endpoint, Class<T> valueType) throws XMindException {
		return sendHttpRequest(getHttpRequestBuilder(getURI(endpoint)).GET().build(), valueType);
	}

	public <T> T put(String endpoint, Object objRequestBody, Class<T> valueType) throws XMindException {
		return sendHttpRequest(getHttpRequestBuilder(getURI(endpoint))
				.PUT(BodyPublishers.ofString(this.writeValueAsString(objRequestBody))).build(),
				valueType);
	}

	public <T> T post(String endpoint, Object objRequestBody, Class<T> valueType) throws XMindException {
		return sendHttpRequest(getHttpRequestBuilder(getURI(endpoint))
				.POST(BodyPublishers.ofString(this.writeValueAsString(objRequestBody))).build(), valueType);
	}

	public <T> T delete(String endpoint, Object objRequestBody, Class<T> valueType) throws XMindException {
		return sendHttpRequest(
				getHttpRequestBuilder(getURI(endpoint))
						.method("DELETE", BodyPublishers.ofString(this.writeValueAsString(objRequestBody))).build(),
				valueType);
	}

	public <T> T delete(String endpoint, Class<T> valueType) throws XMindException {
		return sendHttpRequest(getHttpRequestBuilder(getURI(endpoint)).DELETE().build(), valueType);
	}

	public <T> T patch(String endpoint, Object objRequestBody, Class<T> valueType) throws XMindException {
		return sendHttpRequest(getHttpRequestBuilder(getURI(endpoint))
				.method("PATCH", BodyPublishers.ofString(this.writeValueAsString(objRequestBody))).build(),
				valueType);
	}

	private <T> T sendHttpRequest(HttpRequest request, Class<T> valueType) throws XMindException {
		try {
			var response = this.httpClient.send(request, BodyHandlers.ofString());
			return checkStatusCode(response, valueType);
		} catch (XMindException e) {
			throw e;
		} catch (IOException e) {
			throw new ServerException(e, Constants.UNKNOWN_ERROR_MSG, "0", 500, 0);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new ServerException(e, Constants.UNKNOWN_ERROR_MSG, "0", 500, 0);
		}
	}

	private Builder getHttpRequestBuilder(URI uri) {
		return HttpRequest.newBuilder(uri)
			.timeout(Duration.ofSeconds(Constants.REQUEST_TIMEOUT))
			.headers(HEADERS);
	}

	private URI getURI(String endpoint) {
		return URI.create(this.host + endpoint);
	}

	private <T> T checkStatusCode(HttpResponse<String> response, Class<T> valueType) throws XMindException, JsonProcessingException {
		if (response.statusCode() >= 500) {
			throw new ServerException(Constants.UNKNOWN_ERROR_MSG, "0", response.statusCode(), 0);
		} else if (response.statusCode() >= 400) {
			Parser.parseResponse(this.readValue(response.body(), BaseError.class));
		}
		return this.readValue(response.body(), valueType);
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token.setJwtToken(token);
		updateTokenHeader();
	}

	/**
	 * @param refreshToken the refreshToken to set
	 */
	public void setRefreshToken(String refreshToken) {
		this.token.setRefreshToken(refreshToken);
	}

	/**
	 * @return the refreshToken of the instance
	 */
	public String getRefreshToken() {
		return this.token.getRefreshToken();
	}

	/*
	 * Clear the JWTToken
	 */
	public void clearJwtToken() {
		this.token.setJwtToken(null);
		updateTokenHeader();
	}

	private void updateTokenHeader() {
		HEADERS[7] = Constants.HEADER_AUTHORIZATION_VALUE + this.token.getJwtToken();
	}

	private void updateUserAgentHeader() {
		HEADERS[1] = Constants.HEADER_USER_AGENT_VALUE + this.userAgent;
	}
}
