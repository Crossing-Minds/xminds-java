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
import com.crossingminds.xminds.api.exception.WrongDataException;
import com.crossingminds.xminds.api.exception.XmindsException;
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
	private RootAccount rootAccount;
	private IndividualAccount individualAccount;
	private ServiceAccount serviceAccount;

	@BeforeAll
	public void setUp() {
		System.out.println("Setting Up Tests!!");
		rootAccount = new RootAccount("ing.juliob@gmail.com", "MPyNg3F5EpFIMLOt");
		individualAccount = new IndividualAccount("", "", "John", "Smith", "johnsmith@mymail.com", "P@ssw@ord",
				"manager", false, "", "");
		serviceAccount = new ServiceAccount("", "", "myapp-srv", "abc123@#$", "backend", "", "");
		client = new Client();
	}

	@AfterAll
	static void done() {
		System.out.println("All tests executed!!!");
	}

	@Test
	@Order(1)
	final void testLoginRootAuthError() throws XmindsException {
		RootAccount rootAcct = new RootAccount("ing.juliob@gmail.com", "WrongP@ssword");
		Assertions.assertThrows(AuthenticationException.class, () -> {
			client.loginRoot(rootAcct);
		});
	}

	@Test
	@Order(2)
	final void testLoginRoot() throws XmindsException {
		Token response = client.loginRoot(rootAccount);
		Assertions.assertNotNull(response.getToken());
	}

	@Order(3)
	@Test
	final void testDeleteIndividualAccount() throws XmindsException {
		client.deleteIndividualAccount(individualAccount);
	}

	@Order(4)
	@Test
	final void testCreateIndividualAccount() throws XmindsException {
		// Create an individual account
		IndividualAccount response = client.createIndividualAccount(individualAccount);
		// Verify an existing ID in response
		Assertions.assertNotNull(response.getId());
	}

	@Order(5)
	@Test
	final void testDeleteServiceAccount() throws XmindsException {
		client.deleteServiceAccount(serviceAccount);
	}

	@Order(6)
	@Test
	final void testCreateServiceAccount() throws XmindsException {
		// Create a service account
		ServiceAccount response = client.createServiceAccount(serviceAccount);
		// Verify an existing ID in response
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
				.filter(o -> o.getEmail().equals(individualAccount.getEmail())).findFirst().isPresent());
		// Verify if service account created previously is returned
		Assertions.assertTrue(response.getServiceAccounts().stream()
				.filter(o -> o.getName().equals(serviceAccount.getName())).findFirst().isPresent());
	}

	@Test
	final void testLoginIndividual() {
		// Not implemented yet
	}

	@Test
	final void testLoginService() {
		// Not implemented yet
	}

	@Test
	final void testLoginRefreshToken() throws XmindsException {
		Assertions.assertThrows(WrongDataException.class, () -> {
			client.loginRefreshToken();
		});
	}

	@Test
	final void testResendVerificationCode() {
		// Not implemented yet
	}

	@Test
	final void testVerifyAccount() {
		// Not implemented yet
	}

	@Test
	final void testDeleteCurrentAccount() {
		// Not implemented yet
	}

}
