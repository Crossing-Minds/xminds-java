package com.crossingminds.xminds.api;

import com.crossingminds.xminds.api.exception.AuthenticationException;
import com.crossingminds.xminds.api.exception.DuplicatedException;
import com.crossingminds.xminds.api.exception.NotFoundException;
import com.crossingminds.xminds.api.exception.XmindsException;
import com.crossingminds.xminds.api.model.Database;
import com.crossingminds.xminds.api.model.IndividualAccount;
import com.crossingminds.xminds.api.model.Organization;
import com.crossingminds.xminds.api.model.RootAccount;
import com.crossingminds.xminds.api.model.ServiceAccount;
import com.crossingminds.xminds.api.model.Token;

public class Client {

	private EndpointDecorator endpoint;

	/**
	 * Default constructor for Production
	 */
	public Client() {
		super();
		// The class that contains implementation of endpoints
		var endpointImpl = new EndpointImpl();
		// Wrapper class
		endpoint = new EndpointDecorator(endpointImpl);
	}

	/**
	 * Custom constructor for Testing
	 * 
	 * @param stagingHost
	 */
	public Client(String stagingHost) {
		super();
		// The class that contains implementation of endpoints
		var endpointImpl = new EndpointImpl(stagingHost);
		// Wrapper class
		endpoint = new EndpointDecorator(endpointImpl);
	}

	/**
	 * Retrieve all the accounts that belong to the organization of the token.
	 * 
	 * @return organization (individualAccounts list and the serviceAccounts list)
	 * @throws XmindsException
	 */
	public Organization listAllAccounts() throws XmindsException {
		return endpoint.listAllAccounts();
	}

	/**
	 * Create a new account for an individual, identified by an email. To create a
	 * new account it is necessary to have generated a token previously (using
	 * loginRoot, for instance with the root account).
	 * 
	 * @param individualAccount (firstName, lastName, email, password, role)
	 * @return individualAccount (id)
	 * @throws DuplicatedException
	 */
	public IndividualAccount createIndividualAccount(IndividualAccount individualAccount) throws XmindsException {
		return endpoint.createIndividualAccount(individualAccount);
	}

	/**
	 * Delete another individual account by email address that belong to the
	 * organization of the token.
	 * 
	 * @param individualAccount (email)
	 * @throws NotFoundException
	 */
	public void deleteIndividualAccount(IndividualAccount individualAccount) throws XmindsException {
		endpoint.deleteIndividualAccount(individualAccount);
	}

	/**
	 * Login with the root account, without selecting any database. This is useful
	 * to create new databases, or create new accounts.
	 * 
	 * @param rootAccount (email, password)
	 * @return Token
	 * @throws AuthenticationException
	 */
	public Token loginRoot(RootAccount rootAccount) throws XmindsException {
		return endpoint.loginRoot(rootAccount);
	}

	/**
	 * Login on a database with your account, using a refresh token. A new JWT token
	 * and a (potentially new) refresh_token will be created.
	 * 
	 * @return Token
	 * @throws NotFoundException
	 * @throws AuthenticationException
	 * @throws RefreshTokenExpiredException
	 */
	public Token loginRefreshToken() throws XmindsException {
		return endpoint.loginRefreshToken();
	}

	/**
	 * Create a new service account, identified by a service name. To create a new
	 * account it is necessary to have generated a token previously (using
	 * loginRoot, for instance with the root account).
	 * 
	 * @param serviceAccount (name, password, role)
	 * @return serviceAccount (id)
	 * @throws DuplicatedException
	 */
	public ServiceAccount createServiceAccount(ServiceAccount serviceAccount) throws XmindsException {
		return endpoint.createServiceAccount(serviceAccount);
	}

	/**
	 * Delete another service account by name that belong to the organization of the
	 * token.
	 * 
	 * @param serviceAccount (email)
	 * @throws NotFoundException
	 */
	public void deleteServiceAccount(ServiceAccount serviceAccount) throws XmindsException {
		endpoint.deleteServiceAccount(serviceAccount);
	}

	/**
	 * Login on a database with your account, using your email and password
	 * combination.
	 * 
	 * @param individualAccount (email, password, dbId)
	 * @return Token
	 * @throws AuthenticationException
	 */
	public Token loginIndividual(IndividualAccount individualAccount) throws XmindsException {
		return endpoint.loginIndividual(individualAccount);
	}

	/**
	 * Login on a database with a service account, using a service name and password
	 * combination.
	 * 
	 * @param serviceAccount (name, password, dbId, frontendUserId)
	 * @return Token
	 * @throws AuthenticationException
	 */
	public Token loginService(ServiceAccount serviceAccount) throws XmindsException {
		return endpoint.loginService(serviceAccount);
	}

	/**
	 * Useful to send a new verification code to the email address of an individual
	 * account.
	 * 
	 * @param email
	 * @throws NotFoundException
	 * @throws AuthenticationException
	 */
	public void resendVerificationCode(String email) throws XmindsException {
		endpoint.resendVerificationCode(email);
	}

	/**
	 * Useful to verify the email of an individual account. You can’t use an
	 * individual account without verifying the email. If you didn’t receive our
	 * email, please use resendVerificationCode
	 * 
	 * @param code
	 * @param email
	 * @throws NotFoundException
	 * @throws AuthenticationException
	 */
	public void verifyAccount(String code, String email) throws XmindsException {
		endpoint.verifyAccount(code, email);
	}

	/**
	 * Delete the account you’re logged to with your current token.
	 * 
	 * @throws XmindsException
	 */
	public void deleteCurrentAccount() throws XmindsException {
		endpoint.deleteCurrentAccount();
	}

	/**
	 * Create a new database.
	 * 
	 * @param database (name, description, itemIdType, userIdType)
	 * @return database (id)
	 * @throws XmindsException
	 */
	public Database createDatabase(Database database) throws XmindsException {
		return endpoint.createDatabase(database);
	}

}
