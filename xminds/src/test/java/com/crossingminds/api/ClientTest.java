package com.crossingminds.api;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.commons.annotation.Testable;

import com.crossingminds.api.XMindClientImpl.XMindFactory;
import com.crossingminds.api.exception.AuthenticationException;
import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.Database;
import com.crossingminds.api.model.IndividualAccount;
import com.crossingminds.api.model.RootAccount;
import com.crossingminds.api.model.ServiceAccount;
import com.crossingminds.api.model.Token;
import com.crossingminds.api.response.AccountList;
import com.crossingminds.api.response.DatabasePage;
import com.crossingminds.api.response.DatabaseStatus;

@Testable
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class ClientTest {

	private XMindClient client;
	private IndividualAccount individualAccount;
	private ServiceAccount serviceAccount;
	private Database database;
	private String rootEmail;
	private String rootPass;
	private String stagingHost;

	@BeforeAll
	public void setUp() {
		System.out.println("Setting Up Tests!!");
		this.rootEmail = System.getProperty("rootEmail");
		this.rootPass = System.getProperty("rootPass");
		this.stagingHost = System.getProperty("stagingHost");
		this.individualAccount = new IndividualAccount("", "", "Test firstName", "Test lastName",
				"testindividual@mail.com", "testP@ssw@rd1", "manager", false, "", "");
		this.serviceAccount = new ServiceAccount("", "", "myapp-server-test3", "abc123@#$", "backend", "", "1234");
		this.database = new Database("", "", "Example Test DB name", "Example Test DB longer description", "uuid", "uint32", null);
		client = XMindFactory.getClient(this.stagingHost);
	}

	@AfterAll
	public void done() throws XMindException {
		System.out.println("All tests executed!!!");
	}

	@Test
	@Order(1)
	final void testLoginRootAuthError() throws XMindException {
		RootAccount rootAcct = new RootAccount(this.rootEmail, "**WrongPassword**");
		Assertions.assertThrows(XMindException.class, () -> {
			client.loginRoot(rootAcct);
		});
	}

	@Test
	@Order(2)
	final void testLoginRoot() throws XMindException {
		RootAccount rootAccount = new RootAccount(this.rootEmail, this.rootPass);
		Token response = client.loginRoot(rootAccount);
		Assertions.assertNotNull(response.getJwtToken());
	}

	@Order(3)
	@Test
	final void testCreateDatabase() throws XMindException {
		Database response = client.createDatabase(this.database);
		Assertions.assertNotNull(response.getId());
		this.individualAccount.setDbId(response.getId());
		this.serviceAccount.setDbId(response.getId());
		this.database.setId(response.getId());
	}

	@Order(3)
	@Test
	final void testDeleteIndividualAccount() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.deleteIndividualAccount(this.individualAccount);
		});
	}

	@Order(4)
	@Test
	final void testCreateIndividualAccount() throws XMindException {
		IndividualAccount response = client.createIndividualAccount(this.individualAccount);
		Assertions.assertNotNull(response.getId());
	}

	@Order(5)
	@Test
	final void testDeleteServiceAccount() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.deleteServiceAccount(this.serviceAccount);
		});
	}

	@Order(6)
	@Test
	final void testCreateServiceAccount() throws XMindException {
		ServiceAccount response = client.createServiceAccount(this.serviceAccount);
		Assertions.assertNotNull(response.getId());
	}

	@Order(7)
	@Test
	final void testListAllAccounts() throws XMindException {
		AccountList response = client.listAllAccounts();
		// Individual Accounts must have 1 element at least
		Assertions.assertNotNull(response.getIndividualAccounts());
		// Service Accounts must have 1 element at least
		Assertions.assertNotNull(response.getServiceAccounts());
		// Verify if individual account created previously is returned
		Assertions.assertTrue(response.getIndividualAccounts().stream()
				.filter(o -> o.getEmail().equals(this.individualAccount.getEmail())).findFirst().isPresent());
		// Verify if service account created previously is returned
		Assertions.assertTrue(response.getServiceAccounts().stream()
				.filter(o -> o.getName().equals(this.serviceAccount.getName())).findFirst().isPresent());
	}

	@Order(8)
	@Test
	final void testLoginRefreshTokenWrong() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.loginRefreshToken();
		});
	}

	@Order(9)
	@Test
	final void testLoginIndividual() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.loginIndividual(this.individualAccount);
		});
	}

	@Order(10)
	@Test
	final void testLoginService() throws XMindException {
		Token response = client.loginService(this.serviceAccount);
		Assertions.assertNotNull(response.getRefreshToken());
	}

	@Order(11)
	@Test
	final void testLoginRefreshToken() throws XMindException {
		Token response = client.loginRefreshToken();
		Assertions.assertNotNull(response.getJwtToken());
	}

	@Order(12)
	@Test
	final void testResendVerificationCode() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.resendVerificationCode("john@example.com");
		});
	}

	@Order(13)
	@Test
	final void testVerifyAccount() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.verifyAccount("abcd@efg", "wrongmail@mail.com");
		});
	}

	@Order(14)
	@Test
	final void listAllDatabases() throws XMindException {
		DatabasePage response = client.listAllDatabases();
		// Databases must have 1 element at least
		Assertions.assertTrue(response.getDatabases().size()>0);
		// Verify that database created previously is returned
		Assertions.assertTrue(response.getDatabases().stream()
				.filter(o -> o.getId().equals(this.database.getId())).findFirst().isPresent());
	}

	@Order(15)
	@Test
	final void getCurrentDatabase() throws XMindException {
		Database response = client.getCurrentDatabase();
		// Verify that current database Id is the same that created in previous test
		Assertions.assertEquals(response.getId(), this.database.getId());
	}

	@Order(16)
	@Test
	final void getCurrentDatabaseStatus() throws XMindException {
		DatabaseStatus response = client.getCurrentDatabaseStatus();
		Assertions.assertTrue("ready".equalsIgnoreCase(response.getStatus()) || "pending".equalsIgnoreCase(response.getStatus()));
	}

	@Order(17)
	@Test
	final void deleteCurrentDatabase() throws XMindException {
		Assertions.assertThrows(AuthenticationException.class, () -> {
			client.loginIndividual(individualAccount);
			client.deleteCurrentDatabase();
		});
	}

	@Order(18)
	@Test
	final void testDeleteAccountsException() throws XMindException {
		RootAccount rootAccount = new RootAccount(this.rootEmail, this.rootPass);
		client.loginRoot(rootAccount);
		client.deleteIndividualAccount(this.individualAccount);
		client.deleteServiceAccount(this.serviceAccount);
	}
}

