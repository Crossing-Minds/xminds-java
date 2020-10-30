package com.crossingminds.api;

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
public class DatabaseTest extends BaseMockTest {

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
		Assertions.assertNotNull(databasePage);
		Assertions.assertNotNull(databasePage.getDatabases());
		Assertions.assertTrue(databasePage.getDatabases().size()>0);
		Assertions.assertTrue(databasePage.getNextPage()==4);
		Assertions.assertTrue(databasePage.isHasNext());
		Assertions.assertEquals("wSSZQbPxKvBrk_n2B_m6ZA", databasePage.getDatabases().get(0).getId());
		Assertions.assertEquals("Example DB name", databasePage.getDatabases().get(0).getName());
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
		Assertions.assertNotNull(response.getId());
		Assertions.assertEquals("z3hn6UoSYWtK4KUA", response.getId());
		httpClientMock.verify().post(path).called();
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
		Assertions.assertNotNull(database);
		Assertions.assertNotNull(database.getCounters());
		Assertions.assertTrue(database.getCounters().getRating()==130);
		Assertions.assertTrue(database.getCounters().getUser()==70);
		Assertions.assertTrue(database.getCounters().getItem()==81);
		Assertions.assertEquals("wSSZQbPxKvBrk_n2B_m6ZA", database.getId());
		Assertions.assertEquals("Example DB name", database.getName());
		Assertions.assertEquals("Example DB longer description", database.getDescription());
		Assertions.assertEquals("uint32", database.getUserIdType());
		Assertions.assertEquals("uuid", database.getItemIdType());
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testDeleteteCurrentDatabase() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_DELETE_CURRENT_DATABASE;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.DELETE, path, respMock, "", "");
		client.deleteCurrentDatabase();;
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
		Assertions.assertNotNull(database);
		Assertions.assertNotNull(database.getStatus());
		Assertions.assertEquals("ready", database.getStatus());
		httpClientMock.verify().get(path).called();
	}

}
