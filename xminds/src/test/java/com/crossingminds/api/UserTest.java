package com.crossingminds.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.commons.annotation.Testable;

import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.Property;
import com.crossingminds.api.model.User;
import com.crossingminds.api.utils.Constants;
import com.pgssoft.httpclient.HttpClientMock;
import com.pgssoft.httpclient.internal.HttpMethods;

@Testable
@TestInstance(Lifecycle.PER_CLASS)
public class UserTest extends BaseTest {

	@BeforeAll
	public void setUp() {
		httpClientMock = new HttpClientMock(host);
	}

	@Test
	final void testListAllUserProperties() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LIST_ALL_USER_PROPERTIES;
		var respMock = "{"
				+ "      \"properties\": ["
				+ "          {"
				+ "              \"property_name\": \"age\","
				+ "              \"value_type\": \"int8\","
				+ "              \"repeated\": false"
				+ "          },"
				+ "          {"
				+ "              \"property_name\": \"subscriptions\","
				+ "              \"value_type\": \"unicode32\","
				+ "              \"repeated\": true"
				+ "          }"
				+ "      ]"
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var propertyList = client.listAllUserProperties();
		// check test
		verifyJSONResponse(respMock, propertyList);
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testCreateUserProperty() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CREATE_USER_PROPERTY;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", property.getPropertyName());
		client.createUserProperty(property);
		// check test
		httpClientMock.verify().post(path).withBody(Matchers.containsString(toStringJson(property))).called();
	}

	@Test
	final void testGetUserProperty() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_GET_USER_PROPERTY, property.getPropertyName());
		var respMock = "{"
				+ "      \"property_name\": \"age\","
				+ "      \"value_type\": \"int8\","
				+ "      \"repeated\": false"
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var propertyResponse = client.getUserProperty(property.getPropertyName());
		// check test
		verifyJSONResponse(respMock, propertyResponse);
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testDeleteteUserProperty() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_DELETE_USER_PROPERTY, property.getPropertyName());
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.DELETE, path, respMock, "", "");
		client.deleteUserProperty(property.getPropertyName());
		// check test
		httpClientMock.verify().delete(path).called();
	}

	@Test
	final void testCreateOrUpdateUser() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_CREATE_UPDATE_USER, getUser().getUserId());
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.PUT, path, respMock, "", "");
		client.createOrUpdateUser(getUser());
		// check test
		httpClientMock.verify().put(path).called();
	}

	@Test
	final void testGetUser() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_GET_USER, getUser().getUserId());
		var respMock = "{"
				+ "      \"user\": {"
				+ "          \"subscriptions\": [\"channel1\", \"channel2\"],"
				+ "          \"user_id\": \"123e4567-e89b-12d3-a456-426614174000\","
				+ "          \"age\": 25"
				+ "      }"
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var user = client.getUser(getUser().getUserId());
		// check test
		verifyJSONResponse(respMock, Map.of("user", user));
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testCreateOrUpdateUsersBulk() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CREATE_UPDATE_USERS_BULK;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.PUT, path, respMock, "", "");
		client.createOrUpdateUsersBulk(getUsersBulk(), 1000);
		// check test
		httpClientMock.verify().put(path).called();
	}

	@Test
	final void testListUsers() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LIST_USERS_BY_IDS;
		var respMock = "{"
				+ "      \"users\": ["
				+ "          {"
				+ "              \"user_id\": \"123e4567-e89b-12d3-a456-426614174000\","
				+ "              \"age\": 25,"
				+ "              \"subscriptions\": [\"channel1\", \"channel2\"]"
				+ "          },"
				+ "          {"
				+ "              \"user_id\": \"c3391d83-553b-40e7-818e-fcf658ec397d\","
				+ "              \"age\": 32,"
				+ "              \"subscriptions\": [\"channel1\"]"
				+ "          }"
				+ "      ]"
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", "");
		var users = client.listUsers(getUsersId());
		// check test
		verifyJSONResponse(respMock, Map.of("users", users));
		httpClientMock.verify().post(path).called();
	}

	@Test
	final void testListUsersPaginated() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LIST_USERS_PAGINATED;
		var respMock = "{"
				+ "      \"users\": ["
				+ "          {"
				+ "              \"user_id\": \"123e4567-e89b-12d3-a456-426614174000\","
				+ "              \"age\": 25,"
				+ "              \"subscriptions\": [\"channel1\", \"channel2\"]"
				+ "          },"
				+ "          {"
				+ "              \"user_id\": \"c3391d83-553b-40e7-818e-fcf658ec397d\","
				+ "              \"age\": 32,"
				+ "              \"subscriptions\": [\"channel1\"]"
				+ "          }"
				+ "      ],"
				+ "      \"has_next\": true,"
				+ "      \"next_cursor\": \"Q21vU1pHb1FjSEp...\""
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var usersList = client.listUsersPaginated(10, "Q21vU1pHb1FjSEp...");
		// check test
		verifyJSONResponse(respMock,usersList);
		httpClientMock.verify().get(path+"?cursor=Q21vU1pHb1FjSEp...&amt=10").called();
	}

	// Utils
	Property property = Property.builder().propertyName("age").valueType("int8").repeated(false).build();
	private User getUser() {
		User user = new User();
		user.setUserId("123e4567-e89b-12d3-a456-426614174000");
		user.put("age", 25);
		user.put("subscriptions", Arrays.asList("channel1", "channel2"));
		return user;
	}

	private List<User> getUsersBulk() {
		List<User> users = new ArrayList<>();
		User userA = new User();
		userA.setUserId("123e4567-e89b-12d3-a456-426614174000");
		userA.put("age", 25);
		userA.put("subscriptions", Arrays.asList("channel1", "channel2"));
		users.add(userA);
		User userB = new User();
		userB.setUserId("123e4567-e89b-12d3-a456-426614174000");
		userB.put("age", 25);
		userB.put("subscriptions", Arrays.asList("channel1", "channel2"));
		users.add(userB);
		return users;
	}

	private List<Object> getUsersId() {
		List<Object> usersId = new ArrayList<>();
		usersId.add("123e4567-e89b-12d3-a456-426614174000");
		usersId.add("223e4567-e89b-12d3-a456-426614174001");
		usersId.add("323e4567-e89b-12d3-a456-426614174002");
		usersId.add("423e4567-e89b-12d3-a456-426614174003");
		usersId.add("523e4567-e89b-12d3-a456-426614174004");
		return usersId;
	}
}
