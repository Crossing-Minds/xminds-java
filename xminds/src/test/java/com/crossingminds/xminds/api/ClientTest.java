package com.crossingminds.xminds.api;

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

import com.crossingminds.xminds.api.exception.AuthenticationException;
import com.crossingminds.xminds.api.exception.ForbiddenException;
import com.crossingminds.xminds.api.exception.NotFoundException;
import com.crossingminds.xminds.api.exception.WrongDataException;
import com.crossingminds.xminds.api.exception.XmindsException;
import com.crossingminds.xminds.api.model.Database;
import com.crossingminds.xminds.api.model.IndividualAccount;
import com.crossingminds.xminds.api.model.Organization;
import com.crossingminds.xminds.api.model.RootAccount;
import com.crossingminds.xminds.api.model.ServiceAccount;
import com.crossingminds.xminds.api.model.Token;

@Testable
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class ClientTest {

	private Client client;
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
		this.serviceAccount = new ServiceAccount("", "", "myapp-server-test", "abc123@#$", "backend", "", "1234");
		this.database = new Database("", "", "Example Test DB name", "Example Test DB longer description", "uuid",
				"uint32");
		client = new Client(this.stagingHost);
	}

	@AfterAll
	public void done() throws XmindsException {
		RootAccount rootAccount = new RootAccount(this.rootEmail, this.rootPass);
		client.loginRoot(rootAccount);
		client.deleteIndividualAccount(this.individualAccount);
		client.deleteServiceAccount(this.serviceAccount);
		System.out.println("All tests executed!!!");
	}

	@Test
	@Order(1)
	final void testLoginRootAuthError() throws XmindsException {
		RootAccount rootAcct = new RootAccount(this.rootEmail, "**WrongPassword**");
		Assertions.assertThrows(AuthenticationException.class, () -> {
			client.loginRoot(rootAcct);
		});
	}

	@Test
	@Order(2)
	final void testLoginRoot() throws XmindsException {
		RootAccount rootAccount = new RootAccount(this.rootEmail, this.rootPass);
		Token response = client.loginRoot(rootAccount);
		Assertions.assertNotNull(response.getToken());
	}

	@Order(3)
	@Test
	final void testCreateDatabase() throws XmindsException {
		Database response = client.createDatabase(this.database);
		Assertions.assertNotNull(response.getId());
		this.individualAccount.setDbId(response.getId());
		this.serviceAccount.setDbId(response.getId());
	}

	@Order(3)
	@Test
	final void testDeleteIndividualAccount() throws XmindsException {
		Assertions.assertThrows(NotFoundException.class, () -> {
			client.deleteIndividualAccount(this.individualAccount);
		});
	}

	@Order(4)
	@Test
	final void testCreateIndividualAccount() throws XmindsException {
		IndividualAccount response = client.createIndividualAccount(this.individualAccount);
		Assertions.assertNotNull(response.getId());
	}

	@Order(5)
	@Test
	final void testDeleteServiceAccount() throws XmindsException {
		Assertions.assertThrows(NotFoundException.class, () -> {
			client.deleteServiceAccount(this.serviceAccount);
		});
	}

	@Order(6)
	@Test
	final void testCreateServiceAccount() throws XmindsException {
		ServiceAccount response = client.createServiceAccount(this.serviceAccount);
		Assertions.assertNotNull(response.getId());
	}

	@Order(7)
	@Test
	final void testListAllAccounts() throws XmindsException {
		Organization response = client.listAllAccounts();
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
	final void testLoginRefreshTokenWrong() throws XmindsException {
		Assertions.assertThrows(WrongDataException.class, () -> {
			client.loginRefreshToken();
		});
	}

	@Order(9)
	@Test
	final void testLoginIndividual() throws XmindsException {
		Assertions.assertThrows(AuthenticationException.class, () -> {
			client.loginIndividual(this.individualAccount);
		});
	}

	@Order(10)
	@Test
	final void testLoginService() throws XmindsException {
		Token response = client.loginService(this.serviceAccount);
		Assertions.assertNotNull(response.getRefreshToken());
	}

	@Order(11)
	@Test
	final void testLoginRefreshToken() throws XmindsException {
		Token response = client.loginRefreshToken();
		Assertions.assertNotNull(response.getToken());
	}

	@Order(12)
	@Test
	final void testResendVerificationCode() throws XmindsException {
		Assertions.assertThrows(NotFoundException.class, () -> {
			client.resendVerificationCode("john@example.com");
		});
	}

	@Order(13)
	@Test
	final void testVerifyAccount() throws XmindsException {
		Assertions.assertThrows(NotFoundException.class, () -> {
			client.verifyAccount("abcd@efg", "wrongmail@mail.com");
		});
	}

	@Order(14)
	@Test
	final void testDeleteAccountsException() throws XmindsException {
		Assertions.assertThrows(ForbiddenException.class, () -> {
			client.deleteIndividualAccount(this.individualAccount);
			client.deleteServiceAccount(this.serviceAccount);
		});
	}

	@Order(15)
	@Test
	final void testDeleteCurrentAccount() throws XmindsException {
		// Not implemented yet
	}

}
