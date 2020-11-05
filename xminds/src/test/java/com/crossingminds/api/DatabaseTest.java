package com.crossingminds.api;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.commons.annotation.Testable;

import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.utils.Constants;
import com.pgssoft.httpclient.HttpClientMock;
import com.pgssoft.httpclient.internal.HttpMethods;

@Testable
@TestInstance(Lifecycle.PER_CLASS)
class DatabaseTest extends BaseTest {

	@BeforeAll
	public void setUp() {
		httpClientMock = new HttpClientMock(host);
	}

	@Test
	final void testListAllDatabases() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LIST_ALL_DATABASES;
		var respMock = "{"
				+ "      \"has_next\": true,"
				+ "      \"next_page\": 4,"
				+ "      \"databases\": ["
				+ "          {"
				+ "              \"id\": \"wSSZQbPxKvBrk_n2B_m6ZA\","
				+ "              \"name\": \"Example DB name\","
				+ "              \"description\": \"Example DB longer description\","
				+ "              \"item_id_type\": \"uuid\","
				+ "              \"user_id_type\": \"uint32\""
				+ "          }"
				+ "      ]"
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var databasePage = client.listAllDatabases();
		// check test
		verifyJSONResponse(respMock, databasePage);
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testCreateDatabase() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CREATE_DATABASE;
		var respMock = "{"
				+ "      \"id\": \"z3hn6UoSYWtK4KUA\""
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", database.getName());
		var response = client.createDatabase(database);
		// check test
		Assertions.assertNotNull(response);
		Assertions.assertEquals("z3hn6UoSYWtK4KUA", response.getId());
		httpClientMock.verify().post(path).withBody(Matchers.containsString(toStringJson(database))).called();
	}

	@Test
	final void testGetCurrentDatabase() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CURRENT_DATABASE;
		var respMock = "{"
				+ "      \"id\": \"wSSZQbPxKvBrk_n2B_m6ZA\","
				+ "      \"name\": \"Example DB name\","
				+ "      \"description\": \"Example DB longer description\","
				+ "      \"item_id_type\": \"uuid\","
				+ "      \"user_id_type\": \"uint32\","
				+ "      \"counters\": {"
				+ "          \"rating\": 130,"
				+ "          \"user\": 70,"
				+ "          \"item\": 81"
				+ "      }"
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var database = client.getCurrentDatabase();
		// check test
		verifyJSONResponse(respMock, database);
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testDeleteteCurrentDatabase() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_DELETE_CURRENT_DATABASE;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.DELETE, path, respMock, "", "");
		client.deleteCurrentDatabase();
		// check test
		httpClientMock.verify().delete(path).called();
	}

	@Test
	final void testGetCurrentDatabaseStatus() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CURRENT_DATABASE_STATUS;
		var respMock = "{"
				+ "      \"status\": \"ready\""
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var database = client.getCurrentDatabaseStatus();
		// check test
		verifyJSONResponse(respMock, database);
		httpClientMock.verify().get(path).called();
	}

}
