package com.crossingminds.xminds.api;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import com.crossingminds.xminds.api.exception.ServerException;
import com.crossingminds.xminds.api.exception.XmindsException;
import com.crossingminds.xminds.api.mapper.JSONObjectMapper;
import com.crossingminds.xminds.api.model.Base;
import com.crossingminds.xminds.api.model.Database;
import com.crossingminds.xminds.api.model.IndividualAccount;
import com.crossingminds.xminds.api.model.Organization;
import com.crossingminds.xminds.api.model.RootAccount;
import com.crossingminds.xminds.api.model.ServiceAccount;
import com.crossingminds.xminds.api.model.Token;
import com.crossingminds.xminds.api.utils.Constants;
import com.crossingminds.xminds.api.utils.StringUtils;

/**
 * 
 * This module implements the requests for all API endpoints. The client handles
 * the logic to automatically get a new JWT token using the refresh token.
 *
 */
public class EndpointImpl implements Endpoint {

	private JSONObjectMapper<Token> tokenMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<Organization> organizationMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<IndividualAccount> individualAccountMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<ServiceAccount> serviceAccountMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<RootAccount> rootAccountMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<Database> databaseMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<Base> baseMapper = new JSONObjectMapper<>();

	/*
	 * Not accessible to final client
	 */
	private Request request;
	private String host;

	public EndpointImpl() {
		super();
		this.request = new Request();
	}

	public EndpointImpl(String host) {
		this();
		this.host = host;
	}

	private URI getURI(String endpoint) {
		if (this.host != null) {
			return URI.create(this.host + endpoint);
		} else {
			return URI.create(Constants.API_URL + endpoint);
		}
	}

	public Organization listAllAccounts() throws XmindsException {
		try {
			var jsonStr = this.request.get(getURI(Constants.ENDPOINT_LIST_ALL_ACCOUNTS));
			return (Organization) Parser.parseResponse(organizationMapper.jsonToObject(jsonStr, Organization.class));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}
	}

	public IndividualAccount createIndividualAccount(IndividualAccount individualAccount) throws XmindsException {

		try {
			var response = this.request.post(getURI(Constants.ENDPOINT_CREATE_INDIVIDUAL_ACCOUNT),
					individualAccountMapper.objectToJson(individualAccount));
			return (IndividualAccount) Parser
					.parseResponse(individualAccountMapper.jsonToObject(response, IndividualAccount.class));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	public void deleteIndividualAccount(IndividualAccount individualAccount) throws XmindsException {

		try {
			var response = this.request.delete(getURI(Constants.ENDPOINT_DELETE_INDIVIDUAL_ACCOUNT),
					individualAccountMapper.objectToJson(individualAccount));
			if (!response.isBlank()) {
				Parser.parseResponse(baseMapper.jsonToObject(response, Base.class));
			}
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	public ServiceAccount createServiceAccount(ServiceAccount serviceAccount) throws XmindsException {

		try {
			var response = this.request.post(getURI(Constants.ENDPOINT_CREATE_SERVICE_ACCOUNT),
					serviceAccountMapper.objectToJson(serviceAccount));
			return (ServiceAccount) Parser
					.parseResponse(serviceAccountMapper.jsonToObject(response, ServiceAccount.class));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	public void deleteServiceAccount(ServiceAccount serviceAccount) throws XmindsException {

		try {
			var response = this.request.delete(getURI(Constants.ENDPOINT_DELETE_SERVICE_ACCOUNT),
					serviceAccountMapper.objectToJson(serviceAccount));
			if (!response.isBlank()) {
				Parser.parseResponse(baseMapper.jsonToObject(response, Base.class));
			}
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	public Token loginIndividual(IndividualAccount individualAccount) throws XmindsException {

		try {
			var response = this.request.post(getURI(Constants.ENDPOINT_LOGIN_INDIVIDUAL_ACCOUNT),
					individualAccountMapper.objectToJson(individualAccount));
			var token = tokenMapper.jsonToObject(response, Token.class);
			this.request.setToken(token.getToken());
			this.request.setRefreshToken(token.getRefreshToken());
			return (Token) Parser.parseResponse(token);
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	public Token loginService(ServiceAccount serviceAccount) throws XmindsException {

		try {
			var response = this.request.post(getURI(Constants.ENDPOINT_LOGIN_SERVICE_ACCOUNT),
					serviceAccountMapper.objectToJson(serviceAccount));
			var token = tokenMapper.jsonToObject(response, Token.class);
			this.request.setToken(token.getToken());
			this.request.setRefreshToken(token.getRefreshToken());
			return (Token) Parser.parseResponse(token);
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	public Token loginRoot(RootAccount rootAccount) throws XmindsException {

		try {
			var response = this.request.post(getURI(Constants.ENDPOINT_LOGIN_ROOT),
					rootAccountMapper.objectToJson(rootAccount));
			var token = tokenMapper.jsonToObject(response, Token.class);
			this.request.setToken(token.getToken());
			return (Token) Parser.parseResponse(token);
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	public Token loginRefreshToken() throws XmindsException {

		try {
			var token = new Token();
			token.setRefreshToken(this.request.getRefreshToken());
			var response = this.request.post(getURI(Constants.ENDPOINT_RENEW_LOGIN_REFRESH_TOKEN),
					tokenMapper.objectToJson(token));
			token = tokenMapper.jsonToObject(response, Token.class);
			this.request.setToken(token.getToken());
			return (Token) Parser.parseResponse(token);
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	public void resendVerificationCode(String email) throws XmindsException {

		try {
			Map<String, String> jsonParams = new HashMap<>();
			jsonParams.put("email", StringUtils.decodeUSASCII(email));
			Parser.parseResponse(this.request.put(getURI(Constants.ENDPOINT_RESEND_EMAIL_VERIFICATION_CODE),
					baseMapper.writeValueAsString(jsonParams)));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	public void verifyAccount(String code, String email) throws XmindsException {

		try {
			Map<String, String> queryParams = new HashMap<>();
			queryParams.put("code", code);
			queryParams.put("email", email);
			var queryString = StringUtils.getDecodedASCIIQueryString(queryParams);
			var uri = URI.create(getURI(Constants.ENDPOINT_VERIFY_EMAIL) + "?" + queryString);
			Parser.parseResponse(this.request.get(uri));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	public void deleteCurrentAccount() throws XmindsException {

		try {
			Parser.parseResponse(this.request.delete(getURI(Constants.ENDPOINT_DELETE_CURRENT_ACCOUNT)));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}

	}

	@Override
	public Database createDatabase(Database database) throws XmindsException {
		try {
			var response = this.request.post(getURI(Constants.ENDPOINT_CREATE_DATABASE),
					databaseMapper.objectToJson(database));
			return (Database) Parser.parseResponse(databaseMapper.jsonToObject(response, Database.class));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
		}
	}

}
