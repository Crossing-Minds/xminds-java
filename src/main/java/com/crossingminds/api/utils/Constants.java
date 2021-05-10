package com.crossingminds.api.utils;

import java.io.IOException;
import java.util.Properties;

public class Constants {

	private Constants() {
	}

	/**
	 * Related to Crossing Minds API
	 */
	public static final String API_URL = "https://api.crossingminds.com/";

	/*
	 * Related to Xminds API
	 * 
	 * @parameter xminds.version
	 */
	private static String xmindsVersion;

	/*
	 * Related to Xminds Java client
	 * 
	 * @parameter java.version
	 */
	private static String clientVersion;

	static {
		try {
			Properties prop = new Properties();
			prop.load(Constants.class.getResourceAsStream("/version.properties"));
			xmindsVersion = prop.getProperty("xminds.version");
			clientVersion = prop.getProperty("java.version");
		} catch (IOException e) {
			xmindsVersion = clientVersion = "";
		}
	}

	/*
	 * Related to the timeout of the HttpRequest in minutes
	 */
	public static final int REQUEST_TIMEOUT = 5;

	/**
	 * Related to Headers
	 */
	public static final String HEADER_USER_AGENT = "User-Agent";
	public static final String HEADER_USER_AGENT_VALUE = String.format("CrossingMinds/%s (Java/%s; JSON) ",
			xmindsVersion, clientVersion);
	public static final String HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HEADER_CONTENT_TYPE_JSON_VALUE = "application/json";
	public static final String HEADER_ACCEPT = "Accept";
	public static final String HEADER_ACCEPT_JSON_VALUE = "application/json";
	public static final String HEADER_AUTHORIZATION = "Authorization";
	public static final String HEADER_AUTHORIZATION_VALUE = "Bearer ";

	/**
	 * Related to EndPoints
	 */
	// Accounts
	public static final String ENDPOINT_LIST_ALL_ACCOUNTS = "organizations/accounts/";
	public static final String ENDPOINT_CREATE_INDIVIDUAL_ACCOUNT = "accounts/individual/";
	public static final String ENDPOINT_DELETE_INDIVIDUAL_ACCOUNT = "accounts/individual/";
	public static final String ENDPOINT_CREATE_SERVICE_ACCOUNT = "accounts/service/";
	public static final String ENDPOINT_DELETE_SERVICE_ACCOUNT = "accounts/service/";
	public static final String ENDPOINT_RESEND_EMAIL_VERIFICATION_CODE = "accounts/resend-verification-code/";
	public static final String ENDPOINT_VERIFY_EMAIL = "accounts/verify/";
	public static final String ENDPOINT_DELETE_CURRENT_ACCOUNT = "accounts/";
	// Login
	public static final String ENDPOINT_LOGIN_INDIVIDUAL_ACCOUNT = "login/individual/";
	public static final String ENDPOINT_LOGIN_SERVICE_ACCOUNT = "login/service/";
	public static final String ENDPOINT_LOGIN_ROOT = "login/root/";
	public static final String ENDPOINT_RENEW_LOGIN_REFRESH_TOKEN = "login/refresh-token/";
	// Database
	public static final String ENDPOINT_CREATE_DATABASE = "databases/";
	public static final String ENDPOINT_LIST_ALL_DATABASES = "databases/";
	public static final String ENDPOINT_CURRENT_DATABASE = "databases/current/";
	public static final String ENDPOINT_DELETE_CURRENT_DATABASE = "databases/current/";
	public static final String ENDPOINT_CURRENT_DATABASE_STATUS = "databases/current/status/";
	// Users
	public static final String ENDPOINT_LIST_ALL_USER_PROPERTIES = "users-properties/";
	public static final String ENDPOINT_CREATE_USER_PROPERTY = "users-properties/";
	public static final String ENDPOINT_GET_USER_PROPERTY = "users-properties/%s/";
	public static final String ENDPOINT_DELETE_USER_PROPERTY = "users-properties/%s/";
	public static final String ENDPOINT_GET_USER = "users/%s/";
	public static final String ENDPOINT_CREATE_UPDATE_USER = "users/%s/";
	public static final String ENDPOINT_CREATE_UPDATE_USERS_BULK = "users-bulk/";
	public static final String ENDPOINT_LIST_USERS_PAGINATED = "users-bulk/";
	public static final String ENDPOINT_LIST_USERS_BY_IDS = "users-bulk/list/";
	public static final String ENDPOINT_PARTIAL_UPDATE_USER = "users/%s/";
	public static final String ENDPOINT_PARTIAL_UPDATE_USERS_BULK = "users-bulk/";
	// Items
	public static final String ENDPOINT_LIST_ALL_ITEM_PROPERTIES = "items-properties/";
	public static final String ENDPOINT_CREATE_ITEM_PROPERTY = "items-properties/";
	public static final String ENDPOINT_GET_ITEM_PROPERTY = "items-properties/%s/";
	public static final String ENDPOINT_DELETE_ITEM_PROPERTY = "items-properties/%s/";
	public static final String ENDPOINT_GET_ITEM = "items/%s/";
	public static final String ENDPOINT_CREATE_UPDATE_ITEM = "items/%s/";
	public static final String ENDPOINT_CREATE_UPDATE_ITEMS_BULK = "items-bulk/";
	public static final String ENDPOINT_LIST_ITEMS_PAGINATED = "items-bulk/";
	public static final String ENDPOINT_LIST_ITEMS_BY_ID = "items-bulk/list/";
	public static final String ENDPOINT_PARTIAL_UPDATE_ITEM = "items/%s/";
	public static final String ENDPOINT_PARTIAL_UPDATE_ITEMS_BULK = "items-bulk/";
	// Rating
	public static final String ENDPOINT_CREATE_UPDATE_RATING = "users/%s/ratings/%s/";
	public static final String ENDPOINT_DELETE_RATING = "users/%s/ratings/%s/";
	public static final String ENDPOINT_LIST_ALL_RATINGS_OF_USER = "users/%s/ratings/";
	public static final String ENDPOINT_CREATE_UPDATE_RATINGS_ONE_USER_BULK = "users/%s/ratings/";
	public static final String ENDPOINT_CREATE_UPDATE_RATINGS_MANY_USERS_BULK = "ratings-bulk/";
	public static final String ENDPOINT_LIST_RATINGS_ALL_USERS_BULK = "ratings-bulk/";
	// Recommendation
	public static final String ENDPOINT_GET_SIMILAR_ITEMS_RECOMMENDATIONS = "recommendation/items/%s/items/";
	public static final String ENDPOINT_GET_SESSION_ITEMS_RECOMMENDATIONS = "recommendation/sessions/items/";
	public static final String ENDPOINT_GET_PROFILE_ITEMS_RECOMMENDATIONS = "recommendation/users/%s/items/";
	// Interactions
	public static final String ENDPOINT_CREATE_ONE_INTERACTION = "users/%s/interactions/%s/";
	public static final String ENDPOINT_CREATE_INTERACTIONS_MANY_USERS_BULK = "interactions-bulk/";

	/**
	 * Related to literals
	 */
	public static final String UNKNOWN_ERROR_MSG = "Unknown error from server";

}
