package com.crossingminds.xminds.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import com.crossingminds.xminds.api.model.Token;
import com.crossingminds.xminds.api.utils.Constants;

/**
 * 
 * This class implements the low level request logic of Crossing Minds API
 *
 */
public class Request {

	/*
	 * Array of Headers to use in all requests
	 */
	private String[] headers;
	/*
	 * Contains data related to security
	 */
	private Token token;

	/*
	 * Base constructor
	 */
	public Request() {
		super();
		this.token = new Token();
		this.headers = new String[8];
		this.headers[0] = Constants.HEADER_USER_AGENT;
		this.headers[1] = Constants.HEADER_USER_AGENT_VALUE;
		this.headers[2] = Constants.HEADER_CONTENT_TYPE;
		this.headers[3] = Constants.HEADER_CONTENT_TYPE_JSON_VALUE;
		this.headers[4] = Constants.HEADER_ACCEPT;
		this.headers[5] = Constants.HEADER_ACCEPT_JSON_VALUE;
		this.headers[6] = Constants.HEADER_AUTHORIZATION;
		this.headers[7] = Constants.HEADER_AUTHORIZATION_VALUE;
	}

	/**
	 * 
	 * @param uri
	 * @param requestBody
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public String get(URI uri, String requestBody) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder(uri)
				.timeout(Duration.ofMinutes(Constants.REQUEST_TIMEOUT))
				.headers(this.headers)
				.method("GET", BodyPublishers.ofString(requestBody))
				.build();

		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, BodyHandlers.ofString());

		return response.body();

	}

	public String get(URI uri) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder(uri)
				.timeout(Duration.ofMinutes(Constants.REQUEST_TIMEOUT))
				.headers(this.headers)
				.GET()
				.build();

		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, BodyHandlers.ofString());

		return response.body();

	}

	public String put(URI uri, String requestBody) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder(uri)
				.timeout(Duration.ofMinutes(Constants.REQUEST_TIMEOUT))
				.headers(this.headers)
				.PUT(BodyPublishers.ofString(requestBody))
				.build();

		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, BodyHandlers.ofString());

		return response.body();

	}

	public String post(URI uri, String requestBody) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder(uri)
				.timeout(Duration.ofMinutes(Constants.REQUEST_TIMEOUT))
				.headers(this.headers)
				.POST(BodyPublishers.ofString(requestBody))
				.build();

		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, BodyHandlers.ofString());

		return response.body();

	}

	public String patch(URI uri, String requestBody) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder(uri)
				.timeout(Duration.ofMinutes(Constants.REQUEST_TIMEOUT))
				.headers(this.headers)
				.method("PATCH", BodyPublishers.ofString(requestBody))
				.build();

		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, BodyHandlers.ofString());

		return response.body();

	}

	public String delete(URI uri, String requestBody) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder(uri)
				.timeout(Duration.ofMinutes(Constants.REQUEST_TIMEOUT))
				.headers(this.headers)
				.method("DELETE", BodyPublishers.ofString(requestBody))
				.build();

		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, BodyHandlers.ofString());

		return response.body();

	}

	public String delete(URI uri) throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder(uri)
				.timeout(Duration.ofMinutes(Constants.REQUEST_TIMEOUT))
				.headers(this.headers)
				.DELETE()
				.build();

		HttpResponse<String> response = HttpClient.newHttpClient()
				.send(request, BodyHandlers.ofString());

		return response.body();

	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token.setToken(token);
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
	 * Clear de JWTToken
	 */
	public void clearJwtToken() {
		this.token.setToken(null);
		updateTokenHeader();
	}

	private void updateTokenHeader() {
		this.headers[7] = Constants.HEADER_AUTHORIZATION_VALUE + this.token.getToken();
	}
}
