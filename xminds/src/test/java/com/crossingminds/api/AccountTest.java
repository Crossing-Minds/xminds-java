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
class AccountTest extends BaseMockTest {

	private static final String EXAMPLE_EMAIL = "john@example.com";

	@BeforeAll
	public void setUp() {
		httpClientMock = new HttpClientMock(host);
	}

	@Test
	final void testListAllAccounts() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LIST_ALL_ACCOUNTS;
		var respMock = "{"
				+ "      \"individual_accounts\": ["
				+ "          {"
				+ "              \"first_name\": \"John\","
				+ "              \"last_name\": \"Doe\","
				+ "              \"email\": \"john@example.com\","
				+ "              \"role\": \"manager\","
				+ "              \"verified\": true"
				+ "          }"
				+ "      ],"
				+ "      \"service_accounts\": ["
				+ "          {"
				+ "              \"name\": \"myapp-server\","
				+ "              \"role\": \"backend\""
				+ "          }"
				+ "      ]"
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var accounts = client.listAllAccounts();
		// check test
		Assertions.assertNotNull(accounts);
		Assertions.assertNotNull(accounts.getIndividualAccounts());
		Assertions.assertNotNull(accounts.getServiceAccounts());
		Assertions.assertTrue(accounts.getIndividualAccounts().size()>0);
		Assertions.assertTrue(accounts.getServiceAccounts().size()>0);
		Assertions.assertEquals(EXAMPLE_EMAIL, accounts.getIndividualAccounts().get(0).getEmail());
		Assertions.assertEquals("myapp-server", accounts.getServiceAccounts().get(0).getName());
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testCreateIndividualAccount() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CREATE_INDIVIDUAL_ACCOUNT;
		var respMock = "{"
				+ "      \"id\": \"z3hn6UoSYWtK4KUA\""
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", individualAccount.getEmail());
		var response = client.createIndividualAccount(individualAccount);
		// check test
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getId());
		Assertions.assertEquals("z3hn6UoSYWtK4KUA", response.getId());
		httpClientMock.verify().post(path).called();
	}

	@Test
	final void testCreateServiceAccount() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CREATE_SERVICE_ACCOUNT;
		var respMock = "{"
				+ "      \"id\": \"z3hn6UoSYWtK4KUA\""
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", serviceAccount.getName());
		var response = client.createServiceAccount(serviceAccount);
		// check test
		Assertions.assertNotNull(response);
		Assertions.assertNotNull(response.getId());
		Assertions.assertEquals("z3hn6UoSYWtK4KUA", response.getId());
		httpClientMock.verify().post(path).called();
	}

	@Test
	final void testDeleteteIndividualAccount() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_DELETE_INDIVIDUAL_ACCOUNT;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.DELETE, path, respMock, "", individualAccount.getEmail());
		client.deleteIndividualAccount(individualAccount);
		// check test
		httpClientMock.verify().delete(path).called();
	}

	@Test
	final void testDeleteteServiceAccount() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_DELETE_SERVICE_ACCOUNT;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.DELETE, path, respMock, "", serviceAccount.getName());
		client.deleteServiceAccount(serviceAccount);
		// check test
		httpClientMock.verify().delete(path).called();
	}

	@Test
	final void testResendVerificationCode() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_RESEND_EMAIL_VERIFICATION_CODE;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.PUT, path, respMock, "", EXAMPLE_EMAIL);
		client.resendVerificationCode(EXAMPLE_EMAIL);
		// check test
		httpClientMock.verify().put(path).called();
	}

	@Test
	final void testVerifyAccount() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_VERIFY_EMAIL;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		client.verifyAccount("abcd1234", EXAMPLE_EMAIL);
		// check test
		httpClientMock.verify().get(path+"?code=abcd1234&email="+EXAMPLE_EMAIL).called();;
	}

	@Test
	final void testDeleteteCurrentAccount() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_DELETE_CURRENT_ACCOUNT;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.DELETE, path, respMock, "", "");
		client.deleteCurrentAccount();
		// check test
		httpClientMock.verify().delete(path).called();
	}

}
