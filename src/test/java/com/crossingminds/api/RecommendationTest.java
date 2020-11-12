package com.crossingminds.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.commons.annotation.Testable;

import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.Filter;
import com.crossingminds.api.model.User;
import com.crossingminds.api.model.UserRating;
import com.crossingminds.api.utils.Constants;
import com.crossingminds.api.utils.StringUtils;
import com.pgssoft.httpclient.HttpClientMock;
import com.pgssoft.httpclient.internal.HttpMethods;

@Testable
@TestInstance(Lifecycle.PER_CLASS)
class RecommendationTest extends BaseTest {

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

	@Test
	final void testFilterToString() throws XMindException {
		List<Filter> filters = getFiltersPlus();
		// Verify filter with int value
		Assertions.assertEquals("price:lt:10", filters.get(0).toString());
		// Verify filter with String value
		Assertions.assertEquals("genres:eq:drama", filters.get(1).toString());
		// Verify filter with null value
		Assertions.assertEquals("poster:notempty", filters.get(2).toString());
		// Verify filter with List<String> value
		Assertions.assertEquals("tags:in:family,fiction", filters.get(3).toString());
		// Verify filter with List<Integer> value
		Assertions.assertEquals("price:in:10,20,30", filters.get(4).toString());
		// Verify filter with String[] value
		Assertions.assertEquals("tags:in:drama,comedy,sport", filters.get(5).toString());
		// Verify filter with Integer[] value
		Assertions.assertEquals("price:in:40,50,60", filters.get(6).toString());
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
		Filter f3 = new Filter("poster","notempty", null);
		Filter f4 = new Filter("tags","in",tags);
		filters.add(f1);
		filters.add(f2);
		filters.add(f3);
		filters.add(f4);
		return filters;
	}

	List<Filter> getFiltersPlus() {
		List<Filter> filters = getFilters();
		List<Integer> pricesList = List.of(10, 20 ,30);
		Filter f5 = new Filter("price", "in", pricesList);
		String[] genresArr = {"drama", "comedy", "sport"};
		Filter f6 = new Filter("tags", "in", genresArr);
		Integer[] pricesArr = {40, 50, 60};
		Filter f7 = new Filter("price", "in", pricesArr);
		filters.add(f5);
		filters.add(f6);
		filters.add(f7);
		return filters;
	}

	User getUser() {
		User user = new User();
		user.setUserId("123e4567-e89b-12d3-a456-426614174000");
		user.put("age", 25);
		user.put("subscriptions", Arrays.asList("channel1", "channel2"));
		return user;
	}

	List<UserRating> getRatings() {
		UserRating rating1 = UserRating.builder().itemId("123e4567-e89b-12d3-a456-426614174001").rating(3.5f).build();
		UserRating rating2 = UserRating.builder().itemId("123e4567-e89b-12d3-a456-426614174002").rating(5.0f).build();
		UserRating rating3 = UserRating.builder().itemId("123e4567-e89b-12d3-a456-426614174003").rating(4.8f).build();
		return Arrays.asList(rating1, rating2, rating3);
	}

}
