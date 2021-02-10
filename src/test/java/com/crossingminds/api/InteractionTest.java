package com.crossingminds.api;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.commons.annotation.Testable;

import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.UserInteraction;
import com.crossingminds.api.utils.Constants;
import com.pgssoft.httpclient.HttpClientMock;
import com.pgssoft.httpclient.internal.HttpMethods;

@Testable
@TestInstance(Lifecycle.PER_CLASS)
class InteractionTest extends BaseTest {

	@BeforeAll
	public void setUp() {
		httpClientMock = new HttpClientMock(host);
	}

	@Test
	final void testCreateOneInteraction() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_CREATE_ONE_INTERACTION, userId, itemId);
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", "");
		client.createInteraction(getInteraction());
		// check test
		httpClientMock.verify().post(path).called();
	}

	@Test
	final void testCreateInteractionsBulk() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CREATE_INTERACTIONS_MANY_USERS_BULK;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", "");
		client.createInteractionsBulk(getInteractionsBulk(), 1000);
		// check test
		httpClientMock.verify().post(path).called();
	}

	// Utils
	String itemId = "123e4567-e89b-12d3-a456-426614174000";
	String userId = "12987882-e76b-13d2-f432-287129187297";

	private UserInteraction getInteraction() {
		return UserInteraction.builder().userId(userId).itemId(itemId).interactionType("productView").timestamp(1588812345).build();
	}

	List<UserInteraction> getInteractionsBulk() {
		UserInteraction userInteraction1 = UserInteraction.builder().userId("1234").itemId("123e4567-e89b-12d3-a456-426614174000").interactionType("productView").timestamp(1588812345).build();
		UserInteraction userInteraction2 = UserInteraction.builder().userId("1234").itemId("c3391d83-553b-40e7-818e-fcf658ec397d").interactionType("productView").timestamp(1588854321).build();
		UserInteraction userInteraction3 = UserInteraction.builder().userId("333").itemId("c3391d83-553b-40e7-818e-fcf658ec397d").interactionType("addToCart").timestamp(1588811111).build();
		return Arrays.asList(userInteraction1, userInteraction2, userInteraction3);
	}

}
