package com.crossingminds.api;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.crossingminds.api.exception.InstantiationException;
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
import com.crossingminds.api.model.UserInteraction;
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
 * Note: we have implemented an uncommon pattern using reflection alongside with 
 * a kind of Builder Pattern to solve a problem we have encountered when extending
 * the client from from outside.
 * (it does not fired @LoginRequired logic when we inherit from another project)
 * 
 *
 */
public class XMindClientImpl implements XMindClient {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3509868835866839052L;
	private static final String AMT = "amt";
	private static final String RATINGS = "ratings";
	private static final String FILTERS = "filters";
	private static final String CURSOR = "cursor";
	private static final String INTERACTIONS = "interactions";

	/*
	 * Not accessible to final client
	 */
	protected Request request;

	protected XMindClientImpl(HttpClient httpClient, String host, ServiceAccount serviceAccount, String externalUserAgent) throws XMindException {
		this.request = new Request(httpClient, host, externalUserAgent);
		if(serviceAccount != null)
			this.loginService(serviceAccount);
	}

	/**
	 * 
	 * XmindBuilder to build XmindClient instances
	 *
	 */
	public static class XmindBuilder implements InvocationHandler {

		private static XMindClientImpl xmindClient;
		private String host = "";
		private String userAgent = "";
		private HttpClient httpClient;
		private ServiceAccount serviceAccount;

		public XmindBuilder() {
			super();
		}

		private XmindBuilder(XMindClientImpl client) {
			xmindClient = client;
		}

		public XmindBuilder withHost(String aHost) {
			this.host = aHost;
			return this;
		}

		public XmindBuilder withUserAgent(String anUserAgent) {
			this.userAgent = anUserAgent;
			return this;
		}

		public XmindBuilder withHttpClient(HttpClient aHttpClient) {
			this.httpClient = aHttpClient;
			return this;
		}

		public XmindBuilder withServiceAccount(ServiceAccount aServiceAccount) {
			this.serviceAccount = aServiceAccount;
			return this;
		}

		private boolean hasLoginRequired(Method method) {
			return (null != method.getAnnotation(LoginRequired.class))
					|| (null != MethodUtils.getMatchingMethod(xmindClient.getClass(), method.getName(),
									ArrayUtils.nullToEmpty(method.getParameterTypes()))
							.getAnnotation(LoginRequired.class));
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

		/**
		 * Default builder
		 * 
		 * @return XMindClient - default client instance
		 * @throws XMindException
		 */
		public XMindClient build() throws XMindException {
			return (XMindClient) Proxy.newProxyInstance(XMindClient.class.getClassLoader(),
				new Class<?>[] { XMindClient.class }, new XmindBuilder(
					new XMindClientImpl(this.httpClient, this.host, this.serviceAccount, this.userAgent)));
		}

		/**
		 * Custom builder
		 * 
		 * @param customInterface
		 * @param customImplementation
		 * @return XMindClient - custom client instance
		 * @throws InstantiationException
		 */
		public XMindClient build(Class<?> customInterface, Class<?> customImplementation) throws InstantiationException {
			XMindClientImpl client;
			try {
				// Get a Constructor object reflecting the public constructor with the expected signature. Given an implementation
				Constructor<?> constructor = customImplementation.getConstructor(HttpClient.class, String.class, ServiceAccount.class, String.class);
				// Create a new instance of the customImplementation using the constructor obtained previously
				client = (XMindClientImpl) constructor.newInstance(this.httpClient, this.host, this.serviceAccount, this.userAgent);
				// Get an instance of the proxy class for the custom interface provided
				return (XMindClient) Proxy.newProxyInstance(customInterface.getClassLoader(), new Class<?>[] { customInterface }, new XmindBuilder(client));
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException 
					| SecurityException | java.lang.InstantiationException e) {
				throw new InstantiationException(e, "An error occurred while creating the instance.");
			}
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
		if (response.getRefreshToken() != null) {
			this.request.setRefreshToken(response.getRefreshToken());
		}
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
	public void createOrUpdateUsersBulk(List<User> users, Integer chunkSize, Boolean waitForCompletion) throws XMindException {
		if(chunkSize == null)
			chunkSize = 1000; // default value
		for (List<User> usersChunk : ListUtils.partition(users, chunkSize)) {
			if(waitForCompletion != null) {
				Map<String, Object> bodyParams = new HashMap<>();
				bodyParams.put("users", usersChunk);
				bodyParams.put("wait_for_completion", waitForCompletion);
				this.request.put(Constants.ENDPOINT_CREATE_UPDATE_USERS_BULK, bodyParams, Base.class);
			} else {
				this.request.put(Constants.ENDPOINT_CREATE_UPDATE_USERS_BULK, Map.of("users", usersChunk), Base.class);
			}
		}
	}

	@LoginRequired
	public UserBulk listUsersPaginated(Integer amt, String cursor) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(CURSOR, cursor);
		queryParams.put(AMT, amt);
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
	public void partialUpdateUser(User user, boolean createIfMissing) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_PARTIAL_UPDATE_USER, user.getUserId());
		user.remove("user_id");
		Map<String, Object> bodyParams = new HashMap<>();
		bodyParams.put("user", user);
		bodyParams.put("create_if_missing", createIfMissing);
		this.request.patch(uri, bodyParams, Base.class);
	}

	@LoginRequired
	public void partialUpdateUsersBulk(List<User> users, Integer chunkSize, Boolean waitForCompletion,
			boolean createIfMissing) throws XMindException {
		if(chunkSize == null)
			chunkSize = 1000; // default value
		for (List<User> usersChunk : ListUtils.partition(users, chunkSize)) {
			Map<String, Object> bodyParams = new HashMap<>();
			bodyParams.put("users", usersChunk);
			if(waitForCompletion != null) {
				bodyParams.put("wait_for_completion", waitForCompletion);
			}
			bodyParams.put("create_if_missing", createIfMissing);
			this.request.patch(Constants.ENDPOINT_PARTIAL_UPDATE_USERS_BULK, bodyParams, Base.class);
		}
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
		var uri = String.format(Constants.ENDPOINT_GET_ITEM_PROPERTY, propertyName);
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
	public void createOrUpdateItemsBulk(List<Item> items, Integer chunkSize, Boolean waitForCompletion) throws XMindException {
		if(chunkSize == null)
			chunkSize = 1000; // default value
		for (List<Item> itemsChunk : ListUtils.partition(items, chunkSize)) {
			if(waitForCompletion != null) {
				Map<String, Object> bodyParams = new HashMap<>();
				bodyParams.put("items", itemsChunk);
				bodyParams.put("wait_for_completion", waitForCompletion);
				this.request.put(Constants.ENDPOINT_CREATE_UPDATE_ITEMS_BULK, bodyParams, Base.class);
			} else {
				this.request.put(Constants.ENDPOINT_CREATE_UPDATE_ITEMS_BULK, Map.of("items", itemsChunk), Base.class);
			}
		}
	}

	@LoginRequired
	public ItemBulk listItemsPaginated(Integer amt, String cursor) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(CURSOR, cursor);
		queryParams.put(AMT, amt);
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
	public void partialUpdateItem(Item item, boolean createIfMissing) throws XMindException {
		var uri = String.format(Constants.ENDPOINT_PARTIAL_UPDATE_ITEM, item.getItemId());
		item.remove("item_id");
		Map<String, Object> bodyParams = new HashMap<>();
		bodyParams.put("item", item);
		bodyParams.put("create_if_missing", createIfMissing);
		this.request.patch(uri, bodyParams, Base.class);
	}

	@LoginRequired
	public void partialUpdateItemsBulk(List<Item> items, Integer chunkSize, Boolean waitForCompletion,
			boolean createIfMissing) throws XMindException {
		if(chunkSize == null)
			chunkSize = 1000; // default value
		for (List<Item> itemsChunk : ListUtils.partition(items, chunkSize)) {
			Map<String, Object> bodyParams = new HashMap<>();
			bodyParams.put("items", itemsChunk);
			if(waitForCompletion != null) {
				bodyParams.put("wait_for_completion", waitForCompletion);
			}
			bodyParams.put("create_if_missing", createIfMissing);
			this.request.patch(Constants.ENDPOINT_PARTIAL_UPDATE_ITEMS_BULK, bodyParams, Base.class);
		}
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
	public UserRatingPage listUserRatings(Object userId, Integer page, Integer amt) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put("page", page);
		queryParams.put(AMT, amt);
		return this.request.get(String.format(Constants.ENDPOINT_LIST_ALL_RATINGS_OF_USER, userId) + 
				StringUtils.getEncodedQueryString(queryParams), UserRatingPage.class);
	}

	@LoginRequired
	public void createOrUpdateOneUserRatingsBulk(Object userId, List<UserRating> ratings, Integer chunkSize) throws XMindException {
		if(chunkSize == null)
			chunkSize = 1000; // default value
		for (List<UserRating> ratingsChunk : ListUtils.partition(ratings, chunkSize))
			this.request.put(String.format(Constants.ENDPOINT_CREATE_UPDATE_RATINGS_ONE_USER_BULK, userId), 
					Map.of(RATINGS, ratingsChunk), Base.class);
	}

	@LoginRequired
	public void createOrUpdateRatingsBulk(List<UserRating> userRatings, Integer chunkSize) throws XMindException {
		if(chunkSize == null)
			chunkSize = 4096; // default value
		for (List<UserRating> userRatingsChunk : ListUtils.partition(userRatings, chunkSize))
			this.request.put(Constants.ENDPOINT_CREATE_UPDATE_RATINGS_MANY_USERS_BULK, Map.of(RATINGS, userRatingsChunk), Base.class);
	}

	@LoginRequired
	public UserRatingBulk listRatings(Integer amt, String cursor) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(CURSOR, cursor);
		queryParams.put(AMT, amt);
		var uri = Constants.ENDPOINT_LIST_RATINGS_ALL_USERS_BULK + StringUtils.getEncodedQueryString(queryParams);
		return this.request.get(uri, UserRatingBulk.class);
	}

	@LoginRequired
	public Recommendation getRecommendationsItemToItems(Object itemId, Integer amt, String cursor, List<Filter> filters)
			throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(CURSOR, cursor);
		queryParams.put(AMT, amt);
		queryParams.put(FILTERS, filters);
		var uri = String.format(Constants.ENDPOINT_GET_SIMILAR_ITEMS_RECOMMENDATIONS, itemId) + StringUtils.getEncodedQueryString(queryParams);
		return this.request.get(uri, Recommendation.class);
	}

	@LoginRequired
	public Recommendation getRecommendationsSessionToItems(List<UserRating> ratings, User userProperties, Integer amt,
			String cursor, List<Filter> filters, boolean excludeRatedItems) throws XMindException {
		Map<String, Object> bodyParams = new HashMap<>();
		bodyParams.put(RATINGS, ratings);
		bodyParams.put("user_properties", userProperties);
		if(cursor != null)
			bodyParams.put(CURSOR, cursor);
		if(amt != null)
			bodyParams.put(AMT, amt);
		bodyParams.put(FILTERS, filters);
		bodyParams.put("exclude_rated_items", true);
		var uri = Constants.ENDPOINT_GET_SESSION_ITEMS_RECOMMENDATIONS;
		return this.request.post(uri, bodyParams, Recommendation.class);
	}

	@LoginRequired
	public Recommendation getRecommendationsUserToItems(Object userId, Integer amt, String cursor, List<Filter> filters,
			boolean excludeRatedItems) throws XMindException {
		Map<String, Object> queryParams = new HashMap<>();
		queryParams.put(CURSOR, cursor);
		queryParams.put(AMT, amt);
		queryParams.put(FILTERS, filters);
		queryParams.put("exclude_rated_items", true);
		var uri = String.format(Constants.ENDPOINT_GET_PROFILE_ITEMS_RECOMMENDATIONS, userId) + StringUtils.getEncodedQueryString(queryParams);
		return this.request.get(uri, Recommendation.class);
	}

	@LoginRequired
	public void createInteraction(UserInteraction interaction) throws XMindException {
		this.request.post(String.format(Constants.ENDPOINT_CREATE_ONE_INTERACTION, interaction.getUserId(), interaction.getItemId()), 
				interaction, Base.class);
	}

	@LoginRequired
	public void createInteractionsBulk(List<UserInteraction> userInteractions, Integer chunkSize)
			throws XMindException {
		if (chunkSize == null)
			chunkSize = 4096; // default value
		for (List<UserInteraction> interactionsChunk : ListUtils.partition(userInteractions, chunkSize))
			this.request.post(Constants.ENDPOINT_CREATE_INTERACTIONS_MANY_USERS_BULK, Map.of(INTERACTIONS, interactionsChunk), Base.class);

	}

}