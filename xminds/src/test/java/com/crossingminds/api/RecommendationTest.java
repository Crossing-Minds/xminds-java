package com.crossingminds.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.commons.annotation.Testable;

import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.Filter;
import com.crossingminds.api.model.Rating;
import com.crossingminds.api.model.User;
import com.crossingminds.api.utils.Constants;
import com.crossingminds.api.utils.StringUtils;
import com.pgssoft.httpclient.HttpClientMock;
import com.pgssoft.httpclient.internal.HttpMethods;

@Testable
@TestInstance(Lifecycle.PER_CLASS)
public class RecommendationTest extends BaseTest {

	@BeforeAll
	public void setUp() {
		httpClientMock = new HttpClientMock(host);
	}

	@Test
	final void testGetRecommendationsItemToItems() throws Exception {
		// Prepare Test
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("cursor", null);
		queryParams.put("amt", 10);
		queryParams.put("filters", getFilters());
		var path = "/" + String.format(Constants.ENDPOINT_GET_SIMILAR_ITEMS_RECOMMENDATIONS, itemId);
		var respMock = "{"
				+ "      \"items_id\": ["
				+ "          \"c3391d83-553b-40e7-818e-fcf658ec397d\","
				+ "          \"c2a73584-bbd0-4f04-b497-26bf70152932\""
				+ "      ],"
				+ "      \"next_cursor\": \"HLe-e1Sq...\""
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var ratingList = client.getRecommendationsItemToItems(itemId, 10, null, getFilters());
		// check test
		verifyJSONResponse(respMock, ratingList);
		path += StringUtils.getEncodedQueryString(queryParams);
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testGetRecommendationsSessionToItems() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_GET_SESSION_ITEMS_RECOMMENDATIONS, userId);
		var respMock = "{"
				+ "      \"items_id\": ["
				+ "          \"c3391d83-553b-40e7-818e-fcf658ec397d\","
				+ "          \"c2a73584-bbd0-4f04-b497-26bf70152932\""
				+ "      ],"
				+ "      \"next_cursor\": \"HLe-e1Sq...\""
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", "");
		var ratingList = client.getRecommendationsSessionToItems(getRatings(), getUser(), 10, null, getFilters(), true);
		// check test
		verifyJSONResponse(respMock, ratingList);
		httpClientMock.verify().post(path).called();
	}

	@Test
	final void testGetRecommendationsUserToItems() throws XMindException {
		// Prepare Test
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("cursor", null);
		queryParams.put("amt", 10);
		queryParams.put("filters", getFilters());
		queryParams.put("exclude_rated_items", true);
		var path = "/" + String.format(Constants.ENDPOINT_GET_PROFILE_ITEMS_RECOMMENDATIONS, userId);
		var respMock = "{"
				+ "      \"items_id\": ["
				+ "          \"c3391d83-553b-40e7-818e-fcf658ec397d\","
				+ "          \"c2a73584-bbd0-4f04-b497-26bf70152932\""
				+ "      ],"
				+ "      \"next_cursor\": \"HLe-e1Sq...\""
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var ratingList = client.getRecommendationsUserToItems(userId, 10, null, getFilters(), true);
		// check test
		verifyJSONResponse(respMock, ratingList);
		path += StringUtils.getEncodedQueryString(queryParams);
		httpClientMock.verify().get(path).called();
	}

	// Utils
	String itemId = "123e4567-e89b-12d3-a456-426614174000";
	String userId = "12987882-e76b-13d2-f432-287129187297";
	List<Filter> getFilters() {
		List<Filter> filters = new ArrayList<>();
		Filter f1 = new Filter("price", "lt", 10);
		Filter f2 = new Filter("genres","eq","drama");
		List<String> tags = new ArrayList<>();
		tags.add("family");
		tags.add("fiction");
		Filter f3 = new Filter("tags","in",tags);
		Filter f4 = new Filter("poster","notempty", null);
		filters.add(f1);
		filters.add(f2);
		filters.add(f3);
		filters.add(f4);
		return filters;
	}

	User getUser() {
		User user = new User();
		user.setUserId("123e4567-e89b-12d3-a456-426614174000");
		user.put("age", 25);
		user.put("subscriptions", Arrays.asList("channel1", "channel2"));
		return user;
	}

	List<Rating> getRatings() {
		Rating rating1 = Rating.builder().itemId("123e4567-e89b-12d3-a456-426614174001").rating(3.5f).build();
		Rating rating2 = Rating.builder().itemId("123e4567-e89b-12d3-a456-426614174002").rating(5.0f).build();
		Rating rating3 = Rating.builder().itemId("123e4567-e89b-12d3-a456-426614174003").rating(4.8f).build();
		return Arrays.asList(rating1, rating2, rating3);
	}
}
