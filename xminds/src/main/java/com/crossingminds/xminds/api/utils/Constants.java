package com.crossingminds.xminds.api.utils;

public class Constants {

	/**
	 * Related to Crossing Mids API
	 */
	public static final String API_URL = "https://staging-api.crossingminds.com/";
	public static final String API_VERSION = "v1";

	/*
	 * Related to Xminds Java API
	 * @parameter expression="${project.version}"
	 */
	private static String JAVA_VERSION;

	/*
	 * Related to the timeout of the HttpRequest in minutes
	 */
	public static final int REQUEST_TIMEOUT = 5;
	/**
	 * Related to Headers
	 */
	public static final String HEADER_USER_AGENT = 							"User-Agent";
	public static final String HEADER_USER_AGENT_VALUE = 					"CrossingMinds/" + API_VERSION + " (Java/" + JAVA_VERSION + "; JSON)";
	public static final String HEADER_CONTENT_TYPE = 						"Content-Type";
	public static final String HEADER_CONTENT_TYPE_JSON_VALUE = 			"application/json";
	public static final String HEADER_ACCEPT = 								"Accept";
	public static final String HEADER_ACCEPT_JSON_VALUE = 					"application/json";
	public static final String HEADER_AUTHORIZATION = 						"Authorization";
	public static final String HEADER_AUTHORIZATION_VALUE = 				"Bearer ";

	/**
	 * Related to EndPoints
	 */
	public static final String ENDPOINT_LIST_ALL_ACCOUNTS = 				API_URL + "organizations/accounts/";
	public static final String ENDPOINT_CREATE_INDIVIDUAL_ACCOUNT = 		API_URL + "accounts/individual/";
	public static final String ENDPOINT_DELETE_INDIVIDUAL_ACCOUNT = 		API_URL + "accounts/individual/";
	public static final String ENDPOINT_CREATE_SERVICE_ACCOUNT = 			API_URL + "accounts/service/";
	public static final String ENDPOINT_DELETE_SERVICE_ACCOUNT = 			API_URL + "accounts/service/";
	public static final String ENDPOINT_LOGIN_INDIVIDUAL_ACCOUNT = 			API_URL + "login/individual/";
	public static final String ENDPOINT_LOGIN_SERVICE_ACCOUNT = 			API_URL + "login/service/";
	public static final String ENDPOINT_LOGIN_ROOT = 						API_URL + "login/root/";
	public static final String ENDPOINT_RENEW_LOGIN_REFRESH_TOKEN = 		API_URL + "login/refresh-token/";
	public static final String ENDPOINT_RESEND_EMAIL_VERIFICATION_CODE = 	API_URL + "accounts/resend-verification-code/";
	public static final String ENDPOINT_VERIFY_EMAIL = 				API_URL + "accounts/verify/";
	public static final String ENDPOINT_DELETE_CURRENT_ACCOUNT = 			API_URL + "accounts/";

}
