package com.crossingminds.api;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.commons.annotation.Testable;

import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.UserRating;
import com.crossingminds.api.utils.Constants;
import com.pgssoft.httpclient.HttpClientMock;
import com.pgssoft.httpclient.internal.HttpMethods;

@Testable
@TestInstance(Lifecycle.PER_CLASS)
public class RatingTest extends BaseTest {

	@BeforeAll
	public void setUp() {
		httpClientMock = new HttpClientMock(host);
	}

	@Test
	final void testCreateOrUpdateRating() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_CREATE_UPDATE_RATING, userId, itemId);
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.PUT, path, respMock, "", "");
		client.createOrUpdateRating(userId, getRating());
		// check test
		httpClientMock.verify().put(path).called();
	}

	@Test
	final void testDeleteRating() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_DELETE_RATING, userId, itemId);
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.DELETE, path, respMock, "", "");
		client.deleteRating(userId, itemId);
		// check test
		httpClientMock.verify().delete(path).called();
	}

	@Test
	final void testListUserRatings() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_CREATE_UPDATE_RATINGS_ONE_USER_BULK, userId);
		var respMock = "{"
				+ "      \"has_next\": false,"
				+ "      \"next_page\": 2,"
				+ "      \"user_ratings\": ["
				+ "          {\"item_id\": \"123e4567-e89b-12d3-a456-426614174000\", \"rating\": 8.5, \"timestamp\": 1588812345},"
				+ "          {\"item_id\": \"c3391d83-553b-40e7-818e-fcf658ec397d\", \"rating\": 2.0, \"timestamp\": 1588854321}"
				+ "      ]"
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var ratingList = client.listUserRatings(userId, 1, 10);
		// check test
		verifyJSONResponse(respMock, ratingList);
		httpClientMock.verify().get("/users/12987882-e76b-13d2-f432-287129187297/ratings/?amt=10&page=1").called();
	}

	@Test
	final void testCreateOrUpdateOneUserRatingsBulk() throws XMindException {
		// Prepare Test
		var chunkSize = 1000;
		var path = "/" + String.format(Constants.ENDPOINT_CREATE_UPDATE_RATINGS_ONE_USER_BULK, userId);
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.PUT, path, respMock, "", "");
		client.createOrUpdateOneUserRatingsBulk(userId, getRatings(), chunkSize);
		// check test
		httpClientMock.verify().put(path).called();
	}

	@Test
	final void testCreateOrUpdateRatingsBulk() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CREATE_UPDATE_RATINGS_MANY_USERS_BULK;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.PUT, path, respMock, "", "");
		client.createOrUpdateRatingsBulk(getRatingsBulk(), 1000);
		// check test
		httpClientMock.verify().put(path).called();
	}

	@Test
	final void testListRatings() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LIST_RATINGS_ALL_USERS_BULK;
		var respMock = "{"
				+ "      \"has_next\": true,"
				+ "      \"next_cursor\": \"Q21vU1pHb1FjSEp...\","
				+ "      \"ratings\": ["
				+ "          {\"user_id\": 1234, \"item_id\": \"123e4567-e89b-12d3-a456-426614174000\", \"rating\": 8.5, \"timestamp\": 1588812345},"
				+ "          {\"user_id\": 1234, \"item_id\": \"c3391d83-553b-40e7-818e-fcf658ec397d\", \"rating\": 2.0, \"timestamp\": 1588854321},"
				+ "          {\"user_id\": 333, \"item_id\": \"c3391d83-553b-40e7-818e-fcf658ec397d\", \"rating\": 5.5, \"timestamp\": 1588811111}"
				+ "      ]"
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var ratings = client.listRatings(10, null);
		// check test
		verifyJSONResponse(respMock, ratings);
		httpClientMock.verify().get(path+"?amt=10").called();
	}

	// Utils
	String itemId = "123e4567-e89b-12d3-a456-426614174000";
	String userId = "12987882-e76b-13d2-f432-287129187297";
	UserRating getRating() {
		return UserRating.builder().itemId(itemId).rating(8.5f).timestamp(1588812345).build();
	}

	List<UserRating> getRatingsBulk() {
		UserRating userRating = UserRating.builder().userId("12987882-e76b-13d2-f432-287129187297").itemId("123e4567-e89b-12d3-a416-426614174101").rating(1.6f).timestamp(1588812322).build();
		UserRating userRating1 = UserRating.builder().userId("12987882-e76b-13d2-f432-287129187297").itemId("123e4567-e89b-12d3-a426-426614174201").rating(6.6f).timestamp(1588812311).build();
		UserRating userRating2 = UserRating.builder().userId("12987882-e76b-13d2-f432-287129187297").itemId("123e4567-e89b-12d3-a436-426614174301").rating(6.3f).timestamp(1588812300).build();
		UserRating userRating3 = UserRating.builder().userId("12987882-e76b-13d2-f432-287129187297").itemId("123e4567-e89b-12d3-a446-426614174401").rating(6.2f).timestamp(1588812322).build();
		UserRating userRating4 = UserRating.builder().userId("12980000-e76b-13d2-f432-287129187200").itemId("123e4567-e89b-12d3-a456-426614175101").rating(1.6f).timestamp(1588814000).build();
		UserRating userRating5 = UserRating.builder().userId("12980000-e76b-13d2-f432-287129187200").itemId("123e4567-e89b-12d3-a456-426614176201").rating(6.6f).timestamp(1588814011).build();
		UserRating userRating6 = UserRating.builder().userId("12980000-e76b-13d2-f432-287129187200").itemId("123e4567-e89b-12d3-a456-426614177301").rating(6.3f).timestamp(1588814022).build();
		UserRating userRating7 = UserRating.builder().userId("12980000-e76b-13d2-f432-287129187200").itemId("123e4567-e89b-12d3-a456-426614178401").rating(6.2f).timestamp(1588814023).build();
		return Arrays.asList(userRating, userRating1, userRating2, userRating3, userRating4, userRating5, userRating6, userRating7);
	}

	List<UserRating> getRatings() {
		UserRating rating1 = UserRating.builder().itemId("123e4567-e89b-12d3-a456-426614174001").rating(3.5f).timestamp(1588812322).build();
		UserRating rating2 = UserRating.builder().itemId("123e4567-e89b-12d3-a456-426614174002").rating(5.0f).timestamp(1580002333).build();
		UserRating rating3 = UserRating.builder().itemId("123e4567-e89b-12d3-a456-426614174002").rating(4.8f).timestamp(1580002344).build();
		return Arrays.asList(rating1, rating2, rating3);
	}

}
