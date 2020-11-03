package com.crossingminds.api;

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
class LoginRefreshTokenTest extends BaseTest {

	@BeforeAll
	public void setUp() {
		httpClientMock = new HttpClientMock(host);
	}

	@Test
	final void testAutoLoginRefreshToken() throws XMindException {

		// Prepare Test
		
		// Related to Accounts (GET) 
		// This endpoint returns a JwtTokenExpiredError
		var path = "/" + Constants.ENDPOINT_LIST_ALL_ACCOUNTS;
		var respMock = "{"
				+ 		"\"error_code\":22,"
				+ 		"\"error_name\":\"JwtTokenExpired\","
				+ 		"\"message\":\"The JWT token has expired: The JWT token has expired.\","
				+ 		"\"error_data\":{"
				+ 						"\"error\":\"The JWT token has expired\","
				+ 						"\"name\":\"INCORRECT_JWT_TOKEN\""
				+ 						"}"
				+ 		"}";
		setUpHttpClientMockException(HttpMethods.GET, path, respMock, "", "");

		// Related to LoginRefreshToken (POST)
		var loginRefreshTokenPath = "/" + Constants.ENDPOINT_RENEW_LOGIN_REFRESH_TOKEN;
		var respMockToken = "{"
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
		setUpHttpClientMock(HttpMethods.POST, loginRefreshTokenPath, respMockToken, "", "");
		
		// call Endpoint
		try {
			// Fails because GET is configured to always return JwtTokenExpiredError for this test
			client.listAllAccounts();
		} catch(Exception e) {
			// Nothing to do
		}

		// check test
		// Verify that this endpoint was called twice
		httpClientMock.verify().get(path).called(2);
		// Verify that this endpoint was called once
		httpClientMock.verify().post(loginRefreshTokenPath).called();
	}

}
