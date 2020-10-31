package com.crossingminds.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.platform.commons.annotation.Testable;

import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.Item;
import com.crossingminds.api.model.Property;
import com.crossingminds.api.utils.Constants;
import com.pgssoft.httpclient.HttpClientMock;
import com.pgssoft.httpclient.internal.HttpMethods;

@Testable
@TestInstance(Lifecycle.PER_CLASS)
public class ItemTest extends BaseTest {

	@BeforeAll
	public void setUp() {
		httpClientMock = new HttpClientMock(host);
	}

	@Test
	final void testListAllItemProperties() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LIST_ALL_ITEM_PROPERTIES;
		var respMock = "{"
				+ "      \"properties\": ["
				+ "          {"
				+ "              \"property_name\": \"price\","
				+ "              \"value_type\": \"float32\","
				+ "              \"repeated\": false"
				+ "          },"
				+ "          {"
				+ "              \"property_name\": \"tags\","
				+ "              \"value_type\": \"unicode32\","
				+ "              \"repeated\": true"
				+ "          },"
				+ "          {"
				+ "              \"property_name\": \"genres\","
				+ "              \"value_type\": \"unicode16\","
				+ "              \"repeated\": true"
				+ "          }"
				+ "      ]"
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var propertyList = client.listAllItemProperties();
		// check test
		verifyJSONResponse(respMock, propertyList);
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testCreateItemProperty() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CREATE_ITEM_PROPERTY;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", property.getPropertyName());
		client.createItemProperty(property);
		// check test
		httpClientMock.verify().post(path).withBody(Matchers.containsString(toStringJson(property))).called();
	}

	@Test
	final void testGetItemProperty() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_GET_ITEM_PROPERTY, property.getPropertyName());
		var respMock = "{"
				+ "      \"property_name\": \"price\","
				+ "      \"value_type\": \"float32\","
				+ "      \"repeated\": false"
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var propertyResponse = client.getItemProperty(property.getPropertyName());
		// check test
		verifyJSONResponse(respMock, propertyResponse);
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testDeleteteItemProperty() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_DELETE_ITEM_PROPERTY, property.getPropertyName());
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.DELETE, path, respMock, "", "");
		client.deleteItemProperty(property.getPropertyName());
		// check test
		httpClientMock.verify().delete(path).called();
	}

	@Test
	final void testCreateOrUpdateItem() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_CREATE_UPDATE_ITEM, getItem().getItemId());
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.PUT, path, respMock, "", "");
		client.createOrUpdateItem(getItem());
		// check test
		httpClientMock.verify().put(path).called();
	}

	@Test
	final void testGetItem() throws XMindException {
		// Prepare Test
		var path = "/" + String.format(Constants.ENDPOINT_GET_ITEM, getItem().getItemId());
		var respMock = "{"
				+ "      \"item\": {"
				+ "          \"item_id\": \"123e4567-e89b-12d3-a456-426614174000\","
				+ "          \"tags\": [\"family\", \"sci-fi\"],"
				+ "          \"genres\": [\"drama\", \"comedy\"],"
				+ "          \"price\": 9.99"
				+ "      }"
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var item = client.getItem(getItem().getItemId());
		// check test
		verifyJSONResponse(respMock, Map.of("item", item));
		httpClientMock.verify().get(path).called();
	}

	@Test
	final void testCreateOrUpdateItemsBulk() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_CREATE_UPDATE_ITEMS_BULK;
		var respMock = "";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.PUT, path, respMock, "", "");
		client.createOrUpdateItemsBulk(getItemsBulk(), 1000);
		// check test
		httpClientMock.verify().put(path).called();
	}

	@Test
	final void testListItems() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LIST_ITEMS_BY_ID;
		var respMock = "{"
				+ "      \"items\": ["
				+ "          {"
				+ "              \"item_id\": \"123e4567-e89b-12d3-a456-426614174000\","
				+ "              \"price\": 9.99,"
				+ "              \"tags\": [\"family\", \"sci-fi\"],"
				+ "              \"genres\": [\"drama\", \"comedy\"]"
				+ "          },"
				+ "          {"
				+ "              \"item_id\": \"c3391d83-553b-40e7-818e-fcf658ec397d\","
				+ "              \"price\": 4.49,"
				+ "              \"tags\": [\"family\"],"
				+ "              \"genres\": [\"thriller\"]"
				+ "          }"
				+ "      ]"
				+ "  }";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.POST, path, respMock, "", "");
		var items = client.listItems(getItemsId());
		// check test
		verifyJSONResponse(respMock, Map.of("items", items));
		httpClientMock.verify().post(path).called();
	}

	@Test
	final void testListItemsPaginated() throws XMindException {
		// Prepare Test
		var path = "/" + Constants.ENDPOINT_LIST_ITEMS_PAGINATED;
		var respMock = "{"
				+ "      \"items\": ["
				+ "          {"
				+ "              \"item_id\": \"123e4567-e89b-12d3-a456-426614174000\","
				+ "              \"price\": 9.99,"
				+ "              \"tags\": [\"family\", \"sci-fi\"],"
				+ "              \"genres\": [\"drama\", \"comedy\"]"
				+ "          },"
				+ "          {"
				+ "              \"item_id\": \"c3391d83-553b-40e7-818e-fcf658ec397d\","
				+ "              \"price\": 4.49,"
				+ "              \"tags\": [\"family\"],"
				+ "              \"genres\": [\"thriller\"]"
				+ "          }"
				+ "      ],"
				+ "      \"has_next\": true,"
				+ "      \"next_cursor\": \"Q21vU1pHb1FjSEp...\""
				+ "  	}";
		// call Endpoint
		setUpHttpClientMock(HttpMethods.GET, path, respMock, "", "");
		var itemsList = client.listItemsPaginated(10, "Q21vU1pHb1FjSEp...");
		// check test
		verifyJSONResponse(respMock,itemsList);
		httpClientMock.verify().get(path+"?cursor=Q21vU1pHb1FjSEp...&amt=10").called();
	}

	// Utils
	Property property = Property.builder().propertyName("price").valueType("float32").repeated(false).build();
	Item getItem() {
		Item item = new Item();
		item.setItemId("123e4567-e89b-12d3-a456-426614174000");
		item.put("tags", Arrays.asList("family", "sci-fi"));
		item.put("genres", Arrays.asList("drama", "comedy"));
		item.put("price", 9.99f);
		return item;
	}

	List<Item> getItemsBulk() {
		List<Item> items = new ArrayList<>();
		Item itemA = new Item();
		itemA.setItemId("123e4567-e89b-12d3-a456-426614174000");
		itemA.put("tags", Arrays.asList("family", "sci-fi"));
		itemA.put("genres", Arrays.asList("drama", "comedy"));
		itemA.put("price", 9.99f);
		items.add(itemA);
		Item itemB = new Item();
		itemB.setItemId("c3391d83-553b-40e7-818e-fcf658ec397d");
		itemB.put("tags", Arrays.asList("family"));
		itemB.put("genres", Arrays.asList("thriller"));
		itemA.put("price", 4.49f);
		items.add(itemB);
		return items;
	}

	List<Object> getItemsId() {
		List<Object> itemsId = new ArrayList<>();
		itemsId.add("123e4567-e89b-12d3-a456-426614174000");
		itemsId.add("223e4567-e89b-12d3-a456-426614174001");
		itemsId.add("323e4567-e89b-12d3-a456-426614174002");
		itemsId.add("423e4567-e89b-12d3-a456-426614174003");
		itemsId.add("523e4567-e89b-12d3-a456-426614174004");
		return itemsId;
	}

}
