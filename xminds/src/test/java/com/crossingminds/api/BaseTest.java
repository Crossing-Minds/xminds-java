package com.crossingminds.api;

import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.crossingminds.api.model.Database;
import com.crossingminds.api.model.IndividualAccount;
import com.crossingminds.api.model.RootAccount;
import com.crossingminds.api.model.ServiceAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pgssoft.httpclient.HttpClientMock;
import com.pgssoft.httpclient.internal.HttpMethods;

class BaseTest {

	// Package attributes
	XMindClient client;
	HttpClientMock httpClientMock;
	String host = "http://localhost";
	final ObjectMapper mapper = new ObjectMapper();
	// Accounts
	IndividualAccount individualAccount = IndividualAccount.builder().email("testindividual@mail.com")
			.password("testP@ssw@rd1").dbId("wSSZQbPxKvBrk_n2B_m6ZA").build();
	ServiceAccount serviceAccount = ServiceAccount.builder().name("myapp-server").password("abc123@#$")
			.dbId("wSSZQbPxKvBrk_n2B_m6ZA").build();
	RootAccount rootAccount = RootAccount.builder().email("email_test@my_app.com").password("MyP433w0rD").build();
	Database database = Database.builder().name("Example Test DB name")
			.description("Example Test DB longer description").userIdType("uint32").itemIdType("uuid").build();
	
	// Package methods
	void setUpHttpClientMock(String method, String path, String respMock, String queryParams, String bodyParam) {
		switch (method) {
		case HttpMethods.GET:
			if (!queryParams.isBlank()) {
				String[] params = queryParams.split(":");
				httpClientMock.onGet().withPath(path).withParameter(params[0], params[1]).doReturnJSON(respMock);
			} else if (!bodyParam.isBlank()) {
				httpClientMock.onGet().withPath(path).withBody(containsString(bodyParam)).doReturnJSON(respMock);
			} else {
				httpClientMock.onGet().withPath(path).doReturnJSON(respMock);
			}
			break;
		case HttpMethods.POST:
			if (!queryParams.isBlank()) {
				String[] params = queryParams.split(":");
				httpClientMock.onPost().withPath(path).withParameter(params[0], params[1]).doReturnJSON(respMock);
			} else if (!bodyParam.isBlank()) {
				httpClientMock.onPost().withPath(path).withBody(containsString(bodyParam)).doReturnJSON(respMock);
			} else {
				httpClientMock.onPost().withPath(path).doReturnJSON(respMock);
			}
			break;
		case HttpMethods.PUT:
			if (!queryParams.isBlank()) {
				String[] params = queryParams.split(":");
				httpClientMock.onPut().withPath(path).withParameter(params[0], params[1]).doReturnJSON(respMock);
			} else if (!bodyParam.isBlank()) {
				httpClientMock.onPut().withPath(path).withBody(containsString(bodyParam)).doReturnJSON(respMock);
			} else {
				httpClientMock.onPut().withPath(path).doReturnJSON(respMock);
			}
			break;
		case HttpMethods.DELETE:
			if (!queryParams.isBlank()) {
				String[] params = queryParams.split(":");
				httpClientMock.onDelete().withPath(path).withParameter(params[0], params[1]).doReturnJSON(respMock);
			} else if (!bodyParam.isBlank()) {
				httpClientMock.onDelete().withPath(path).withBody(containsString(bodyParam)).doReturnJSON(respMock);
			} else {
				httpClientMock.onDelete().withPath(path).doReturnJSON(respMock);
			}
			break;
		}
		client = XMindClientImpl.XMindFactory.getClient(httpClientMock, host + "/");
	}

	public void verifyJSONResponse(String expected, Object received) {
		try {
			Assertions.assertNotNull(received);
			JSONAssert.assertEquals(expected, this.toStringJson(received), JSONCompareMode.STRICT);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
	}

	String toStringJson(Object obj) {
		try {
			return this.mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
