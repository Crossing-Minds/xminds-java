package com.crossingminds.xminds.api;

import com.crossingminds.xminds.api.exception.JwtTokenExpiredException;
import com.crossingminds.xminds.api.exception.XmindsException;
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
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				return endpoint.listAllAccounts();
			}
			throw ex;
		}
	}

	@Override
	public IndividualAccount createIndividualAccount(IndividualAccount individualAccount) throws XmindsException {
		try {
			return endpoint.createIndividualAccount(individualAccount);
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				return endpoint.createIndividualAccount(individualAccount);
			}
			throw ex;
		}
	}

	@Override
	public void deleteIndividualAccount(IndividualAccount individualAccount) throws XmindsException {
		try {
			endpoint.deleteIndividualAccount(individualAccount);
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				endpoint.deleteIndividualAccount(individualAccount);
			}
			throw ex;
		}
	}

	@Override
	public Token loginRoot(RootAccount rootAccount) throws XmindsException {
		try {
			return endpoint.loginRoot(rootAccount);
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				return endpoint.loginRoot(rootAccount);
			}
			throw ex;
		}
	}

	@Override
	public Token loginRefreshToken() throws XmindsException {
		try {
			return endpoint.loginRefreshToken();
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				return endpoint.loginRefreshToken();
			}
			throw ex;
		}
	}

	@Override
	public ServiceAccount createServiceAccount(ServiceAccount serviceAccount) throws XmindsException {
		try {
			return endpoint.createServiceAccount(serviceAccount);
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				return endpoint.createServiceAccount(serviceAccount);
			}
			throw ex;
		}
	}

	@Override
	public void deleteServiceAccount(ServiceAccount serviceAccount) throws XmindsException {
		try {
			endpoint.deleteServiceAccount(serviceAccount);
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				endpoint.deleteServiceAccount(serviceAccount);
			}
			throw ex;
		}
	}

	@Override
	public Token loginIndividual(IndividualAccount individualAccount) throws XmindsException {
		try {
			return endpoint.loginIndividual(individualAccount);
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				return endpoint.loginIndividual(individualAccount);
			}
			throw ex;
		}
	}

	@Override
	public Token loginService(ServiceAccount serviceAccount) throws XmindsException {
		try {
			return endpoint.loginService(serviceAccount);
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				return endpoint.loginService(serviceAccount);
			}
			throw ex;
		}
	}

	@Override
	public void resendVerificationCode(String email) throws XmindsException {
		try {
			endpoint.resendVerificationCode(email);
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				endpoint.resendVerificationCode(email);
			}
			throw ex;
		}
	}

	@Override
	public void verifyAccount(String code, String email) throws XmindsException {
		try {
			endpoint.verifyAccount(code, email);
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				endpoint.verifyAccount(code, email);
			}
			throw ex;
		}
	}

	@Override
	public void deleteCurrentAccount() throws XmindsException {
		try {
			endpoint.deleteCurrentAccount();
		} catch (XmindsException ex) {
			if (ex instanceof JwtTokenExpiredException) {
				endpoint.loginRefreshToken();
				endpoint.deleteCurrentAccount();
			}
			throw ex;
		}
	}

}
