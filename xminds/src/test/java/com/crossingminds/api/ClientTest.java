package com.crossingminds.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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
import com.crossingminds.api.model.Property;
import com.crossingminds.api.model.RootAccount;
import com.crossingminds.api.model.ServiceAccount;
import com.crossingminds.api.model.Token;
import com.crossingminds.api.model.User;
import com.crossingminds.api.response.AccountList;
import com.crossingminds.api.response.DatabasePage;
import com.crossingminds.api.response.DatabaseStatus;
import com.crossingminds.api.response.UserBulk;

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
		this.database = new Database("", "", "Example Test DB name 2", "Example Test DB longer description 2", "uuid",
				"uint32", null);
		client = XMindFactory.getClient(this.stagingHost);
	}

	@AfterAll
	public void done() throws XMindException {
		System.out.println("All tests executed!!!");
	}

	@Test
	final void testLoginRootAuthError() throws XMindException {
		RootAccount rootAcct = new RootAccount(this.rootEmail, "**WrongPassword**");
		Assertions.assertThrows(XMindException.class, () -> {
			client.loginRoot(rootAcct);
		});
	}

	@Test
	final void testLoginRoot() throws XMindException {
		RootAccount rootAccount = new RootAccount(this.rootEmail, this.rootPass);
		Token response = client.loginRoot(rootAccount);
		Assertions.assertNotNull(response.getJwtToken());
	}

	@Test
	final void testCreateDatabase() throws XMindException {
		Database response = client.createDatabase(this.database);
		Assertions.assertNotNull(response.getId());
		this.individualAccount.setDbId(response.getId());
		this.serviceAccount.setDbId(response.getId());
		this.database.setId(response.getId());
	}

	@Test
	final void testDeleteIndividualAccount() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.deleteIndividualAccount(this.individualAccount);
		});
	}

	@Test
	final void testCreateIndividualAccount() throws XMindException {
		IndividualAccount response = client.createIndividualAccount(this.individualAccount);
		Assertions.assertNotNull(response.getId());
	}

	@Test
	final void testDeleteServiceAccount() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.deleteServiceAccount(this.serviceAccount);
		});
	}

	@Test
	final void testCreateServiceAccount() throws XMindException {
		ServiceAccount response = client.createServiceAccount(this.serviceAccount);
		Assertions.assertNotNull(response.getId());
	}

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

	@Test
	final void testLoginRefreshTokenWrong() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.loginRefreshToken();
		});
	}

	@Test
	final void testLoginIndividual() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.loginIndividual(this.individualAccount);
		});
	}

	@Test
	final void testLoginService() throws XMindException {
		Token response = client.loginService(this.serviceAccount);
		Assertions.assertNotNull(response.getRefreshToken());
	}

	@Test
	final void testLoginRefreshToken() throws XMindException {
		Token response = client.loginRefreshToken();
		Assertions.assertNotNull(response.getJwtToken());
	}

	@Test
	final void testResendVerificationCode() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.resendVerificationCode("john@example.com");
		});
	}

	@Test
	final void testVerifyAccount() throws XMindException {
		Assertions.assertThrows(XMindException.class, () -> {
			client.verifyAccount("abcd@efg", "wrongmail@mail.com");
		});
	}

	@Test
	final void listAllDatabases() throws XMindException {
		DatabasePage response = client.listAllDatabases();
		// Databases must have 1 element at least
		Assertions.assertTrue(response.getDatabases().size() > 0);
		// Verify that database created previously is returned
		Assertions.assertTrue(response.getDatabases().stream().filter(o -> o.getId().equals(this.database.getId()))
				.findFirst().isPresent());
	}

	@Test
	final void getCurrentDatabase() throws XMindException {
		Database response = client.getCurrentDatabase();
		// Verify that current database Id is the same that created in previous test
		Assertions.assertEquals(response.getId(), this.database.getId());
	}

	@Test
	final void getCurrentDatabaseStatus() throws XMindException {
		DatabaseStatus response = client.getCurrentDatabaseStatus();
		Assertions.assertTrue(
				"ready".equalsIgnoreCase(response.getStatus()) || "pending".equalsIgnoreCase(response.getStatus()));
	}

	@Test
	final void deleteCurrentDatabase() throws XMindException {
		Assertions.assertThrows(AuthenticationException.class, () -> {
			client.loginIndividual(individualAccount);
			client.deleteCurrentDatabase();
		});
	}

	@Test
	final void testDeleteAccountsException() throws XMindException {
		RootAccount rootAccount = new RootAccount(this.rootEmail, this.rootPass);
		client.loginRoot(rootAccount);
		client.deleteIndividualAccount(this.individualAccount);
		client.deleteServiceAccount(this.serviceAccount);
	}

	@Test
	final void testListAllUserProperties() throws XMindException {
		var response = client.listAllUserProperties();
		Assertions.assertNotNull(response);
	}

	@Test
	final void testUserProperty() throws XMindException {
		Property prop = new Property("age", "int8", false);
		client.createUserProperty(prop);
		Property property = client.getUserProperty("age");
		Assertions.assertEquals(property.getPropertyName(), prop.getPropertyName());
		client.deleteUserProperty(prop.getPropertyName());
	}

	@Test
	final void testUser() throws XMindException {
		List<String> subscriptions = new ArrayList<>();
		subscriptions.add("music");
		subscriptions.add("sport");
		subscriptions.add("tv");
		User user = User.builder().build();
		user.setUserId(1249836200);
		user.put("age", 42);
		user.put("subscriptions", subscriptions);
		client.createOrUpdateUser(user);
		User userRetrieved = client.getUser(1249836200);
		Assertions.assertEquals(user.getUserId(), userRetrieved.getUserId());
	}

	@Test
	final void testCreateOrUpdateUsersBulk() throws XMindException {
		// User Create/Update Bulk
		User userOne = new User();
		userOne.setUserId(1582728500);
		Map<String, Object> propers = new HashMap<>();
		propers.put("age", 25);
		List<String> subscriptions2 = new ArrayList<>();
		subscriptions2.add("tecno");
		subscriptions2.add("culture");
		subscriptions2.add("family");
		propers.put("subscriptions", subscriptions2);
		userOne.putAll(propers);

		User userTwo = new User();
		userTwo.setUserId(1570884300);
		Map<String, Object> propers2 = new HashMap<>();
		propers2.put("age", 24);
		List<String> subscriptions3 = new ArrayList<>();
		subscriptions3.add("baseball");
		subscriptions3.add("football");
		subscriptions3.add("tenis");
		propers2.put("subscriptions", subscriptions3);
		userTwo.putAll(propers);
		List<User> users = new ArrayList<>();
		users.add(userOne);
		users.add(userTwo);

		client.createOrUpdateUsersBulk(users, 1000);
		
		List<Object> usersId = new ArrayList<>();
		usersId.add(1111678740);
		usersId.add(1212121212);
		usersId.add(1582728500);
		usersId.add(1570884300);
		usersId.add(12498362);
		List<User> response = client.listUsers(usersId);
		Assertions.assertTrue(response != null && response.size()>0);
	}

	@Test
	final void testListUsersBulk() throws XMindException {
		UserBulk response = client.listUsersPaginated(10, null);
		Assertions.assertTrue(response != null && response.getUsers().size()>0);
	}

}
