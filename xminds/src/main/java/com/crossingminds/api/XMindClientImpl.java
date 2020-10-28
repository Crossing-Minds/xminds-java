package com.crossingminds.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang3.reflect.MethodUtils;

import com.crossingminds.api.exception.JwtTokenExpiredException;
import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.Base;
import com.crossingminds.api.model.Database;
import com.crossingminds.api.model.IndividualAccount;
import com.crossingminds.api.model.RootAccount;
import com.crossingminds.api.model.ServiceAccount;
import com.crossingminds.api.model.Token;
import com.crossingminds.api.response.AccountList;
import com.crossingminds.api.response.DatabasePage;
import com.crossingminds.api.response.DatabaseStatus;
import com.crossingminds.api.utils.Constants;
import com.crossingminds.api.utils.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 
 * This module implements the requests for all API endpoints. The client handles
 * the logic to automatically get a new JWT token using the refresh token.
 *
 */
public class XMindClientImpl implements XMindClient {

	/*
	 * Not accessible to final client
	 */
	private Request request;

	private XMindClientImpl() {
		super();
		this.request = new Request("");
	}

	private XMindClientImpl(String staging) {
		this();
		this.request = new Request(staging);
	}

	private String decode(String value) {
		return StringUtils.decodeUSASCII(value);
	}

	/**
	 * 
	 * Factory to create XmindClient instances
	 *
	 */
	public static class XMindFactory implements InvocationHandler {

		private XMindClientImpl xmindClient;

		public XMindFactory(XMindClientImpl client) {
			this.xmindClient = client;
		}

		private boolean hasLoginRequired(Method method) {
			return (null != method.getAnnotation(LoginRequired.class)) || (null != MethodUtils
					.getMatchingMethod(xmindClient.getClass(), method.getName()).getAnnotation(LoginRequired.class));
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				return method.invoke(xmindClient, args);
			} catch (InvocationTargetException e) {
				try {
					throw e.getTargetException();
				} catch (JwtTokenExpiredException ex) {
					if (!this.hasLoginRequired(method))
						throw ex;
					xmindClient.loginRefreshToken();
					return method.invoke(xmindClient, args);
				}
			}
		}

		public static XMindClient getClient() {
			return (XMindClient) Proxy.newProxyInstance(XMindClient.class.getClassLoader(),
					new Class<?>[] { XMindClient.class }, new XMindFactory(new XMindClientImpl()));
		}

		public static XMindClient getClient(String stagingHost) {
			return (XMindClient) Proxy.newProxyInstance(XMindClient.class.getClassLoader(),
					new Class<?>[] { XMindClient.class }, new XMindFactory(new XMindClientImpl(stagingHost)));
		}

	}

	@LoginRequired
	public AccountList listAllAccounts() throws XMindException {
		return this.request.get(Constants.ENDPOINT_LIST_ALL_ACCOUNTS, AccountList.class);
	}

	@LoginRequired
	public IndividualAccount createIndividualAccount(IndividualAccount individualAccount) throws XMindException {
		return this.request.post(Constants.ENDPOINT_CREATE_INDIVIDUAL_ACCOUNT, individualAccount,
				IndividualAccount.class);
	}

	@LoginRequired
	public void deleteIndividualAccount(IndividualAccount individualAccount) throws XMindException {
		this.request.delete(Constants.ENDPOINT_DELETE_INDIVIDUAL_ACCOUNT, individualAccount, Base.class);
	}

	@LoginRequired
	public ServiceAccount createServiceAccount(ServiceAccount serviceAccount) throws XMindException {
		return this.request.post(Constants.ENDPOINT_CREATE_SERVICE_ACCOUNT, serviceAccount, ServiceAccount.class);
	}

	@LoginRequired
	public void deleteServiceAccount(ServiceAccount serviceAccount) throws XMindException {
		this.request.delete(Constants.ENDPOINT_DELETE_SERVICE_ACCOUNT, serviceAccount, Base.class);
	}

	public Token loginIndividual(IndividualAccount individualAccount) throws XMindException {
		var response = this.request.post(Constants.ENDPOINT_LOGIN_INDIVIDUAL_ACCOUNT, individualAccount, Token.class);
		this.request.setToken(response.getJwtToken());
		this.request.setRefreshToken(response.getRefreshToken());
		return response;
	}

	public Token loginService(ServiceAccount serviceAccount) throws XMindException {
		var response = this.request.post(Constants.ENDPOINT_LOGIN_SERVICE_ACCOUNT, serviceAccount, Token.class);
		this.request.setToken(response.getJwtToken());
		this.request.setRefreshToken(response.getRefreshToken());
		return response;
	}

	public Token loginRoot(RootAccount rootAccount) throws XMindException {
		var response = this.request.post(Constants.ENDPOINT_LOGIN_ROOT, rootAccount, Token.class);
		this.request.setToken(response.getJwtToken());
		return response;
	}

	public Token loginRefreshToken() throws XMindException {
		var token = new Token();
		token.setRefreshToken(this.request.getRefreshToken());
		var response = this.request.post(Constants.ENDPOINT_RENEW_LOGIN_REFRESH_TOKEN, token, Token.class);
		this.request.setToken(response.getJwtToken());
		return response;
	}

	public void resendVerificationCode(String email) throws XMindException, JsonProcessingException {
		ObjectNode params = JsonNodeFactory.instance.objectNode();
		params.put("email", email);
		this.request.put(Constants.ENDPOINT_RESEND_EMAIL_VERIFICATION_CODE, params, Base.class);
	}

	public void verifyAccount(String code, String email) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_VERIFY_EMAIL, this.decode(code), this.decode(email));
		this.request.get(uri, Base.class);
	}

	@LoginRequired
	public void deleteCurrentAccount() throws XMindException {
		this.request.delete(Constants.ENDPOINT_DELETE_CURRENT_ACCOUNT, Base.class);
	}

	@LoginRequired
	public Database createDatabase(Database database) throws XMindException {
		return this.request.post(Constants.ENDPOINT_CREATE_DATABASE, database, Database.class);
	}

	@LoginRequired
	public DatabasePage listAllDatabases() throws XMindException {
		return this.request.get(Constants.ENDPOINT_LIST_ALL_DATABASES, DatabasePage.class);
	}

	@LoginRequired
	public Database getCurrentDatabase() throws XMindException {
		return this.request.get(Constants.ENDPOINT_CURRENT_DATABASE, Database.class);
	}

	@LoginRequired
	public void deleteCurrentDatabase() throws XMindException {
		this.request.delete(Constants.ENDPOINT_DELETE_CURRENT_DATABASE, Base.class);
	}

	@LoginRequired
	public DatabaseStatus getCurrentDatabaseStatus() throws XMindException {
		return this.request.get(Constants.ENDPOINT_CURRENT_DATABASE_STATUS, DatabaseStatus.class);
	}

}

