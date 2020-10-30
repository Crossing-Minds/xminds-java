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
class LoginTest extends BaseMockTest {

	@BeforeAll
	public void setUp() {
		httpClientMock = new HttpClientMock(host);
	}

	@Test
	final void testLoginRoot() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LOGIN_ROOT;
		var respMock = "{\"token\":\"eyJ0eX...\"}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", "");
		var token = client.loginRoot(rootAccount);
		// check test
		Assertions.assertNotNull(token.getJwtToken());
		Assertions.assertEquals(respMock, toStringJson(token));
		httpClientMock.verify().post(path).withBody(Matchers.containsString(toStringJson(rootAccount))).called();
	}

	@Test
	final void testLoginIndividual() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LOGIN_INDIVIDUAL_ACCOUNT;
		var respMock = "{"
				+ "      \"token\": \"eyJ0eX...\","
				+ "      \"refresh_token\": \"mW+k/K...\","
				+ "      \"database\" : {"
				+ "        \"id\": \"wSSZQbPxKvBrk_n2B_m6ZA\","
				+ "        \"name\": \"Example DB name\","
				+ "        \"description\": \"Example DB longer description\","
				+ "        \"item_id_type\": \"uuid\","
				+ "        \"user_id_type\": \"uint32\""
				+ "      }"
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", individualAccount.getDbId());
		var token = client.loginIndividual(individualAccount);
		// check test
		verifyJSONResponse(respMock, token);
		httpClientMock.verify().post(path).withBody(Matchers.containsString(toStringJson(individualAccount))).called();
	}

	@Test
	final void loginService() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LOGIN_SERVICE_ACCOUNT;
		var respMock = "{"
				+ "      \"token\": \"eyJ0eX...\","
				+ "      \"refresh_token\": \"mW+k/K...\","
				+ "      \"database\" : {"
				+ "        \"id\": \"wSSZQbPxKvBrk_n2B_m6ZA\","
				+ "        \"name\": \"Example DB name\","
				+ "        \"description\": \"Example DB longer description\","
				+ "        \"item_id_type\": \"uuid\","
				+ "        \"user_id_type\": \"uint32\""
				+ "      }"
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", serviceAccount.getDbId());
		var token = client.loginService(serviceAccount);
		// check test
		verifyJSONResponse(respMock, token);
		httpClientMock.verify().post(path).withBody(Matchers.containsString(toStringJson(serviceAccount))).called();
	}

	@Test
	final void testLoginRefreshToken() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_RENEW_LOGIN_REFRESH_TOKEN;
		var respMock = "{"
				+ "      \"token\": \"eyJ0eX...\","
				+ "      \"refresh_token\": \"mW+k/K...\","
				+ "      \"database\" : {"
				+ "        \"id\": \"wSSZQbPxKvBrk_n2B_m6ZA\","
				+ "        \"name\": \"Example DB name\","
				+ "        \"description\": \"Example DB longer description\","
				+ "        \"item_id_type\": \"uuid\","
				+ "        \"user_id_type\": \"uint32\""
				+ "      }"
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", "");
		var token = client.loginRefreshToken();
		// check test
		verifyJSONResponse(respMock, token);
		httpClientMock.verify().post(path).called();
	}
	
}
