package com.crossingminds.xminds.api;

import com.crossingminds.xminds.api.exception.JwtTokenExpiredException;
import com.crossingminds.xminds.api.exception.XmindsException;
import com.crossingminds.xminds.api.model.Database;
import com.crossingminds.xminds.api.model.IndividualAccount;
import com.crossingminds.xminds.api.model.Organization;
import com.crossingminds.xminds.api.model.RootAccount;
import com.crossingminds.xminds.api.model.ServiceAccount;
import com.crossingminds.xminds.api.model.Token;

public class EndpointDecorator implements Endpoint {

	private final Endpoint endpoint;

	public EndpointDecorator(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public Organization listAllAccounts() throws XmindsException {
		try {
			return endpoint.listAllAccounts();
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			return endpoint.listAllAccounts();
		}
	}

	@Override
	public IndividualAccount createIndividualAccount(IndividualAccount individualAccount) throws XmindsException {
		try {
			return endpoint.createIndividualAccount(individualAccount);
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			return endpoint.createIndividualAccount(individualAccount);
		}
	}

	@Override
	public void deleteIndividualAccount(IndividualAccount individualAccount) throws XmindsException {
		try {
			endpoint.deleteIndividualAccount(individualAccount);
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			endpoint.deleteIndividualAccount(individualAccount);
		}
	}

	@Override
	public Token loginRoot(RootAccount rootAccount) throws XmindsException {
		try {
			return endpoint.loginRoot(rootAccount);
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			return endpoint.loginRoot(rootAccount);
		}
	}

	@Override
	public Token loginRefreshToken() throws XmindsException {
		try {
			return endpoint.loginRefreshToken();
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			return endpoint.loginRefreshToken();
		}
	}

	@Override
	public ServiceAccount createServiceAccount(ServiceAccount serviceAccount) throws XmindsException {
		try {
			return endpoint.createServiceAccount(serviceAccount);
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			return endpoint.createServiceAccount(serviceAccount);
		}
	}

	@Override
	public void deleteServiceAccount(ServiceAccount serviceAccount) throws XmindsException {
		try {
			endpoint.deleteServiceAccount(serviceAccount);
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			endpoint.deleteServiceAccount(serviceAccount);
		}
	}

	@Override
	public Token loginIndividual(IndividualAccount individualAccount) throws XmindsException {
		try {
			return endpoint.loginIndividual(individualAccount);
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			return endpoint.loginIndividual(individualAccount);
		}
	}

	@Override
	public Token loginService(ServiceAccount serviceAccount) throws XmindsException {
		try {
			return endpoint.loginService(serviceAccount);
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			return endpoint.loginService(serviceAccount);
		}
	}

	@Override
	public void resendVerificationCode(String email) throws XmindsException {
		try {
			endpoint.resendVerificationCode(email);
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			endpoint.resendVerificationCode(email);
		}
	}

	@Override
	public void verifyAccount(String code, String email) throws XmindsException {
		try {
			endpoint.verifyAccount(code, email);
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			endpoint.verifyAccount(code, email);
		}
	}

	@Override
	public void deleteCurrentAccount() throws XmindsException {
		try {
			endpoint.deleteCurrentAccount();
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			endpoint.deleteCurrentAccount();
		}
	}

	@Override
	public Database createDatabase(Database database) throws XmindsException {
		try {
			return endpoint.createDatabase(database);
		} catch (JwtTokenExpiredException ex) {
			endpoint.loginRefreshToken();
			return endpoint.createDatabase(database);
		}
	}

}
