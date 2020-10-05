package com.crossingminds.xminds.api;

import java.io.IOException;
import java.net.URI;

import com.crossingminds.xminds.api.exception.AuthenticationException;
import com.crossingminds.xminds.api.exception.DuplicatedException;
import com.crossingminds.xminds.api.exception.NotFoundException;
import com.crossingminds.xminds.api.exception.ServerException;
import com.crossingminds.xminds.api.exception.XmindsException;
import com.crossingminds.xminds.api.mapper.JSONObjectMapper;
import com.crossingminds.xminds.api.model.Base;
import com.crossingminds.xminds.api.model.IndividualAccount;
import com.crossingminds.xminds.api.model.Organization;
import com.crossingminds.xminds.api.model.RootAccount;
import com.crossingminds.xminds.api.model.ServiceAccount;
import com.crossingminds.xminds.api.model.Token;
import com.crossingminds.xminds.api.utils.Constants;

/**
 * 
 * This module implements the requests for all API endpoints. The client handles
 * the logic to automatically get a new JWT token using the refresh token.
 *
 */
public class Client {

	private JSONObjectMapper<Token> tokenMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<Organization> organizationMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<IndividualAccount> individualAccountMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<ServiceAccount> serviceAccountMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<RootAccount> rootAccountMapper = new JSONObjectMapper<>();
	private JSONObjectMapper<Base> baseMapper = new JSONObjectMapper<>();

	/*
	 * Not accessible to final client
	 */
	private Request request;

	public Client() {
		super();
		this.request = new Request();
	}

	/**
	 * Retrieve all the accounts that belong to the organization of the token.
	 * 
	 * @return the Organization.individualAccounts list and the
	 *         Organization.serviceAccounts list
	 * @throws XmindsException
	 */
	public Organization listAllAccounts() throws XmindsException {
		try {
			URI uri = URI.create(Constants.ENDPOINT_LIST_ALL_ACCOUNTS);
			String jsonStr = this.request.get(uri);
			return (Organization) Parser.parseResponse(organizationMapper.jsonToObject(jsonStr, Organization.class));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}
	}

	/**
	 * Create a new account for an individual, identified by an email. To create a
	 * new account it is necessary to have generated a token previously (using
	 * loginRoot, for instance with the root account).
	 * 
	 * @param individualAccount.first_name, individualAccount.last_name,
	 *                                      individualAccount.email,
	 *                                      individualAccount.password,
	 *                                      individualAccount.role
	 * @return individualAccount.id
	 * @throws DuplicatedException
	 */
	public IndividualAccount createIndividualAccount(IndividualAccount individualAccount) throws XmindsException {

		try {
			URI uri = URI.create(Constants.ENDPOINT_CREATE_INDIVIDUAL_ACCOUNT);
			String response = this.request.post(uri, individualAccountMapper.objectToJson(individualAccount));
			return (IndividualAccount) Parser.parseResponse(individualAccountMapper.jsonToObject(response, IndividualAccount.class));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

	/**
	 * Delete another individual account by email address that belong to the
	 * organization of the token.
	 * 
	 * @param individualAccount.email
	 * @throws NotFoundException
	 */
	public void deleteIndividualAccount(IndividualAccount individualAccount) throws XmindsException {

		try {
			URI uri = URI.create(Constants.ENDPOINT_DELETE_INDIVIDUAL_ACCOUNT);
			String response = this.request.delete(uri, individualAccountMapper.objectToJson(individualAccount));
			if(!response.isBlank()) {
				Parser.parseResponse(baseMapper.jsonToObject(response, Base.class));
			}
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

	/**
	 * Create a new service account, identified by a service name. To create a new
	 * account it is necessary to have generated a token previously (using
	 * loginRoot, for instance with the root account).
	 * 
	 * @param serviceAccount.name, serviceAccount.password, serviceAccount.role
	 * @return serviceAccount.id
	 * @throws DuplicatedException
	 */
	public ServiceAccount createServiceAccount(ServiceAccount serviceAccount) throws XmindsException {

		try {
			URI uri = URI.create(Constants.ENDPOINT_CREATE_SERVICE_ACCOUNT);
			String response = this.request.post(uri, serviceAccountMapper.objectToJson(serviceAccount));
			return (ServiceAccount) Parser.parseResponse(serviceAccountMapper.jsonToObject(response, ServiceAccount.class));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

	/**
	 * Delete another service account by name that belong to the organization of the
	 * token.
	 * 
	 * @param serviceAccount.email
	 * @throws NotFoundException
	 */
	public void deleteServiceAccount(ServiceAccount serviceAccount) throws XmindsException {

		try {
			URI uri = URI.create(Constants.ENDPOINT_DELETE_SERVICE_ACCOUNT);
			String response = this.request.delete(uri, serviceAccountMapper.objectToJson(serviceAccount));
			if(!response.isBlank()) {
				Parser.parseResponse(baseMapper.jsonToObject(response, Base.class));
			}
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

	/**
	 * Login on a database with your account, using your email and password
	 * combination.
	 * 
	 * @param individualAccount.email, individualAccount.password,
	 *                                 individualAccount.db_id
	 * @return Token
	 * @throws AuthenticationException
	 */
	public Token loginIndividual(IndividualAccount individualAccount) throws XmindsException {

		try {
			URI uri = URI.create(Constants.ENDPOINT_LOGIN_INDIVIDUAL_ACCOUNT);
			String response = this.request.post(uri, individualAccountMapper.objectToJson(individualAccount));
			Token token = tokenMapper.jsonToObject(response, Token.class);
			this.request.setToken(token.getToken());
			this.request.setRefreshToken(token.getRefreshToken());
			return (Token) Parser.parseResponse(token);
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

	/**
	 * Login on a database with a service account, using a service name and password
	 * combination.
	 * 
	 * @param serviceAccount.name, serviceAccount.password, serviceAccount.db_id,
	 *                             serviceAccount.frontend_user_id
	 * @return Token
	 * @throws AuthenticationException
	 */
	public Token loginService(ServiceAccount serviceAccount) throws XmindsException {

		try {
			URI uri = URI.create(Constants.ENDPOINT_LOGIN_SERVICE_ACCOUNT);
			String response = this.request.post(uri, serviceAccountMapper.objectToJson(serviceAccount));
			Token token = tokenMapper.jsonToObject(response, Token.class);
			this.request.setToken(token.getToken());
			this.request.setRefreshToken(token.getRefreshToken());
			return (Token) Parser.parseResponse(token);
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

	/**
	 * Login with the root account, without selecting any database. This is useful
	 * to create new databases, or create new accounts.
	 * 
	 * @param rootAccount.email, rootAccount.password
	 * @return Token
	 * @throws AuthenticationException
	 */
	public Token loginRoot(RootAccount rootAccount) throws XmindsException {

		try {
			URI uri = URI.create(Constants.ENDPOINT_LOGIN_ROOT);
			String response = this.request.post(uri, rootAccountMapper.objectToJson(rootAccount));
			Token token = tokenMapper.jsonToObject(response, Token.class);
			this.request.setToken(token.getToken());
			return (Token) Parser.parseResponse(token);
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

	/**
	 * Login on a database with your account, using a refresh token. A new JWT token
	 * and a (potentially new) refresh_token will be created.
	 * 
	 * @return Token
	 * @throws NotFoundException, AuthenticationException,
	 *                            RefreshTokenExpiredException
	 */
	public Token loginRefreshToken() throws XmindsException {

		try {
			Token token = new Token();
			token.setRefreshToken(this.request.getRefreshToken());
			URI uri = URI.create(Constants.ENDPOINT_RENEW_LOGIN_REFRESH_TOKEN);
			String response = this.request.post(uri, tokenMapper.objectToJson(token));
			token = tokenMapper.jsonToObject(response, Token.class);
			this.request.setToken(token.getToken());
			return (Token) Parser.parseResponse(token);
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

	/**
	 * Useful to send a new verification code to the email address of an individual
	 * account.
	 * 
	 * @param email
	 * @throws NotFoundException, AuthenticationException
	 */
	public void resendVerificationCode(String email) throws XmindsException {

		try {
			URI uri = URI.create(Constants.ENDPOINT_RESEND_EMAIL_VERIFICATION_CODE);
			Parser.parseResponse(this.request.put(uri, "\"email\":\"" + email + "\""));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

	/**
	 * Useful to verify the email of an individual account. You can’t use an
	 * individual account without verifying the email. If you didn’t receive our
	 * email, please use resendVerificationCode
	 * 
	 * @param code
	 * @param email
	 * @throws NotFoundException, AuthenticationException
	 */
	public void verifyAccount(String code, String email) throws XmindsException {

		try {
			URI uri = URI.create(Constants.ENDPOINT_VERIFY_EMAIL + "?code=" + code + "&email=" + email);
			Parser.parseResponse(this.request.get(uri));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

	/**
	 * Delete the account you’re logged to with your current token.
	 * 
	 * @throws XmindsException
	 */
	public void deleteCurrentAccount() throws XmindsException {

		try {
			URI uri = URI.create(Constants.ENDPOINT_DELETE_CURRENT_ACCOUNT);
			Parser.parseResponse(this.request.delete(uri));
		} catch (IOException | InterruptedException ex) {
			throw new ServerException("Unknown error from server", "0", "500", 0);
		}

	}

}
