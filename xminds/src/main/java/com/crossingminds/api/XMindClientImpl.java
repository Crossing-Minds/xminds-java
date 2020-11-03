package com.crossingminds.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.crossingminds.api.exception.JwtTokenExpiredException;
import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.Base;
import com.crossingminds.api.model.Database;
import com.crossingminds.api.model.Filter;
import com.crossingminds.api.model.IndividualAccount;
import com.crossingminds.api.model.Item;
import com.crossingminds.api.model.Property;
import com.crossingminds.api.model.RootAccount;
import com.crossingminds.api.model.ServiceAccount;
import com.crossingminds.api.model.Token;
import com.crossingminds.api.model.User;
import com.crossingminds.api.model.UserRating;
import com.crossingminds.api.response.AccountList;
import com.crossingminds.api.response.DatabasePage;
import com.crossingminds.api.response.DatabaseStatus;
import com.crossingminds.api.response.ItemBulk;
import com.crossingminds.api.response.ItemList;
import com.crossingminds.api.response.ItemMap;
import com.crossingminds.api.response.PropertyList;
import com.crossingminds.api.response.Recommendation;
import com.crossingminds.api.response.UserBulk;
import com.crossingminds.api.response.UserList;
import com.crossingminds.api.response.UserMap;
import com.crossingminds.api.response.UserRatingBulk;
import com.crossingminds.api.response.UserRatingPage;
import com.crossingminds.api.utils.Constants;
import com.crossingminds.api.utils.StringUtils;
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

	private XMindClientImpl(HttpClient httpClient, String host) {
		this.request = new Request(httpClient, host);
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

		@LoginRequired
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

		// Public constructor (Default)
		public static XMindClient getClient() {
			return (XMindClient) Proxy.newProxyInstance(XMindClient.class.getClassLoader(),
					new Class<?>[] { XMindClient.class }, new XMindFactory(new XMindClientImpl(HttpClient.newHttpClient(), "")));
		}

		// Protected constructor (Development/Test)
		protected static XMindClient getClient(String host) {
			return (XMindClient) Proxy.newProxyInstance(XMindClient.class.getClassLoader(),
					new Class<?>[] { XMindClient.class },
					new XMindFactory(new XMindClientImpl(HttpClient.newHttpClient(), host)));
		}

		// Protected constructor (Mock/Test)
		protected static XMindClient getClient(HttpClient httpClient, String host) {
			return (XMindClient) Proxy.newProxyInstance(XMindClient.class.getClassLoader(),
					new Class<?>[] { XMindClient.class }, new XMindFactory(new XMindClientImpl(httpClient, host)));
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

	public void resendVerificationCode(String email) throws XMindException {
		ObjectNode bodyParams = JsonNodeFactory.instance.objectNode();
		bodyParams.put("email", email);
		this.request.put(Constants.ENDPOINT_RESEND_EMAIL_VERIFICATION_CODE, bodyParams, Base.class);
	}

	public void verifyAccount(String code, String email) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("code", code);
		queryParams.put("email", email);
		var uri = Constants.ENDPOINT_VERIFY_EMAIL + StringUtils.getEncodedQueryString(queryParams);
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

	@LoginRequired
	public PropertyList listAllUserProperties() throws XMindException {
		return this.request.get(Constants.ENDPOINT_LIST_ALL_USER_PROPERTIES, PropertyList.class);
	}

	@LoginRequired
	public void createUserProperty(Property userProperty) throws XMindException {
		this.request.post(Constants.ENDPOINT_CREATE_USER_PROPERTY, userProperty, Base.class);
	}

	@LoginRequired
	public Property getUserProperty(String propertyName) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_GET_USER_PROPERTY, propertyName);
		return this.request.get(uri, Property.class);
	}

	@LoginRequired
	public void deleteUserProperty(String propertyName) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_DELETE_USER_PROPERTY, propertyName);
		this.request.delete(uri, Base.class);
	}

	@LoginRequired
	public User getUser(Object userId) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_GET_USER, userId);
		return this.request.get(uri, UserMap.class).getUser();
	}

	@LoginRequired
	public void createOrUpdateUser(User user) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_CREATE_UPDATE_USER, user.getUserId());
		Map<String, Object> bodyParams = new HashMap<>();
		user.remove("user_id");
		bodyParams.put("user", user);
		this.request.put(uri, bodyParams, Base.class);
	}

	@LoginRequired
	public void createOrUpdateUsersBulk(List<User> users, Integer chunkSize) throws XMindException {
		if(chunkSize == null)
			chunkSize = 1000; // default value
		for (List<User> usersChunk : ListUtils.partition(users, chunkSize))
			this.request.put(Constants.ENDPOINT_CREATE_UPDATE_USERS_BULK, Map.of("users", usersChunk), Base.class);                
	}

	@LoginRequired
	public UserBulk listUsersPaginated(int amt, String cursor) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("amt", amt);
		queryParams.put("cursor", cursor);
		var uri = Constants.ENDPOINT_LIST_USERS_PAGINATED + StringUtils.getEncodedQueryString(queryParams);
		return this.request.get(uri, UserBulk.class);
	}

	@LoginRequired
	public List<User> listUsers(List<Object> usersId) throws XMindException {
		Map<String, Object> bodyParams = new HashMap<>();
		bodyParams.put("users_id", usersId);
		var uri = Constants.ENDPOINT_LIST_USERS_BY_IDS;
		return this.request.post(uri, bodyParams, UserList.class).getUsers();
	}

	@LoginRequired
	public PropertyList listAllItemProperties() throws XMindException {
		return this.request.get(Constants.ENDPOINT_LIST_ALL_ITEM_PROPERTIES, PropertyList.class);
	}

	@LoginRequired
	public void createItemProperty(Property itemProperty) throws XMindException {
		this.request.post(Constants.ENDPOINT_CREATE_ITEM_PROPERTY, itemProperty, Base.class);
	}

	@LoginRequired
	public Property getItemProperty(String propertyName) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_GET_ITEM, propertyName);
		return this.request.get(uri, Property.class);
	}

	@LoginRequired
	public void deleteItemProperty(String propertyName) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_DELETE_ITEM_PROPERTY, propertyName);
		this.request.delete(uri, Base.class);
	}

	@LoginRequired
	public Item getItem(Object itemId) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_GET_ITEM, itemId);
		return this.request.get(uri, ItemMap.class).getItem();
	}

	@LoginRequired
	public void createOrUpdateItem(Item item) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_CREATE_UPDATE_ITEM, item.getItemId());
		Map<String, Object> bodyParams = new HashMap<>();
		item.remove("item_id");
		bodyParams.put("item", item);
		this.request.put(uri, bodyParams, Base.class);
	}

	@LoginRequired
	public void createOrUpdateItemsBulk(List<Item> items, Integer chunkSize) throws XMindException {
		if(chunkSize == null)
			chunkSize = 1000; // default value
		for (List<Item> itemsChunk : ListUtils.partition(items, chunkSize))
			this.request.put(Constants.ENDPOINT_CREATE_UPDATE_ITEMS_BULK, Map.of("items", itemsChunk), Base.class);
	}

	@LoginRequired
	public ItemBulk listItemsPaginated(int amt, String cursor) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("amt", amt);
		queryParams.put("cursor", cursor);
		var uri = Constants.ENDPOINT_LIST_ITEMS_PAGINATED + StringUtils.getEncodedQueryString(queryParams);
		return this.request.get(uri, ItemBulk.class);
	}

	@LoginRequired
	public List<Item> listItems(List<Object> itemsId) throws XMindException {
		Map<String, Object> bodyParams = new HashMap<>();
		bodyParams.put("items_id", itemsId);
		var uri = Constants.ENDPOINT_LIST_ITEMS_BY_ID;
		return this.request.post(uri, bodyParams, ItemList.class).getItems();
	}

	@LoginRequired
	public void createOrUpdateRating(Object userId, UserRating rating) throws XMindException {
		this.request.put(String.format(Constants.ENDPOINT_CREATE_UPDATE_RATING, userId, rating.getItemId()), rating, Base.class);
	}

	@LoginRequired
	public void deleteRating(Object userId, Object itemId) throws XMindException {
		this.request.delete(String.format(Constants.ENDPOINT_DELETE_RATING, userId, itemId), Base.class);
	}

	@LoginRequired
	public UserRatingPage listUserRatings(Object userId, int page, int amt) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("page", page);
		queryParams.put("amt", amt);
		return this.request.get(String.format(Constants.ENDPOINT_LIST_ALL_RATINGS_OF_USER, userId) + 
				StringUtils.getEncodedQueryString(queryParams), UserRatingPage.class);
	}

	@LoginRequired
	public void createOrUpdateOneUserRatingsBulk(Object userId, List<UserRating> ratings, Integer chunkSize) throws XMindException {
		if(chunkSize == null)
			chunkSize = 1000; // default value
		for (List<UserRating> ratingsChunk : ListUtils.partition(ratings, chunkSize))
			this.request.put(String.format(Constants.ENDPOINT_CREATE_UPDATE_RATINGS_ONE_USER_BULK, userId), 
					Map.of("ratings", ratingsChunk), Base.class);
	}

	@LoginRequired
	public void createOrUpdateRatingsBulk(List<UserRating> userRatings, Integer chunkSize) throws XMindException {
		if(chunkSize == null)
			chunkSize = 4096; // default value
		for (List<UserRating> userRatingsChunk : ListUtils.partition(userRatings, chunkSize))
			this.request.put(Constants.ENDPOINT_CREATE_UPDATE_RATINGS_MANY_USERS_BULK, Map.of("ratings", userRatingsChunk), Base.class);
	}

	@LoginRequired
	public UserRatingBulk listRatings(int amt, String cursor) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("amt", amt);
		queryParams.put("cursor", cursor);
		var uri = Constants.ENDPOINT_LIST_RATINGS_ALL_USERS_BULK + StringUtils.getEncodedQueryString(queryParams);
		return this.request.get(uri, UserRatingBulk.class);
	}

	@LoginRequired
	public Recommendation getRecommendationsItemToItems(Object itemId, Integer amt, String cursor, List<Filter> filters)
			throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		if(cursor != null)
			queryParams.put("cursor", cursor);
		if(amt != null)
			queryParams.put("amt", amt);
		queryParams.put("filters", filters);
		var uri = String.format(Constants.ENDPOINT_GET_SIMILAR_ITEMS_RECOMMENDATIONS, itemId) + StringUtils.getEncodedQueryString(queryParams);
		return this.request.get(uri, Recommendation.class);
	}

	@LoginRequired
	public Recommendation getRecommendationsSessionToItems(List<Rating> ratings, User userProperties, Integer amt,
			String cursor, List<Filter> filters, boolean excludeRatedItems) throws XMindException {
		Map<String, Object> bodyParams = new HashMap<String, Object>();
		bodyParams.put("ratings", ratings);
		bodyParams.put("user_properties", userProperties);
		if(cursor != null)
			bodyParams.put("cursor", cursor);
		if(amt != null)
			bodyParams.put("amt", amt);
		bodyParams.put("filters", filters);
		bodyParams.put("exclude_rated_items", true);
		var uri = Constants.ENDPOINT_GET_SESSION_ITEMS_RECOMMENDATIONS;
		return this.request.post(uri, bodyParams, Recommendation.class);
	}

	@LoginRequired
	public Recommendation getRecommendationsUserToItems(Object userId, Integer amt, String cursor, List<Filter> filters,
			boolean excludeRatedItems) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		if(cursor != null)
			queryParams.put("cursor", cursor);
		if(amt != null)
			queryParams.put("amt", amt);
		queryParams.put("filters", filters);
		queryParams.put("exclude_rated_items", true);
		var uri = String.format(Constants.ENDPOINT_GET_PROFILE_ITEMS_RECOMMENDATIONS, userId) + StringUtils.getEncodedQueryString(queryParams);
		return this.request.get(uri, Recommendation.class);
	}

}
