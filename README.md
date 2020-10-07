# Xminds-Java API

Java client for Crossing Minds B2B API

## Requirements

Building the API client library requires:
1. Java 11+ (for development/unit testing it is recommended to use [AdoptJDK](https://adoptopenjdk.net/) free version)
2. Maven 3.6+ (download [here](http://maven.apache.org/download.cgi) then [install](http://maven.apache.org/install.html) and see also how to [use](http://maven.apache.org/guides/getting-started/index.html))

## Installation

To package the API client, execute:
```shell
mvn clean package
```
To install the API client library to your local Maven repository, simply execute:
```shell
mvn clean install
```

## Running Tests

To run tests, execute the command:

```shell
mvn clean test -DrootEmail=<ROOT_EMAIL> -DrootPass=<ROOT_PASSWORD> -DstagingHost=<STAGING_HOST>
```

To achieve the success of the tests it is necessary to provide data from an existing user on the platform.

<ROOT_EMAIL> e.g. user@mail.com

<ROOT_PASSWORD> e.g.: P@ssw@rd

<STAGING_HOST> e.g. https://staging-api.crossingminds.com/

## Desing considerations

The API is built as a Java / Maven project.

For JSON mapping we use the Jackson standard (Java JSON library) (see information [here](https://github.com/FasterXML/jackson)) which is a powerful set of data processing tools for Java (and the JVM platform) so it is well known and used.

To comply with the Jackson standard it is necessary to generate model classes (POJOs) to map each entity of the system with its attributes, but with the ability to map extra attributes through an additional attribute for this purpose.

#### Example of a POJO

```java
// Database class to map Database entity

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "id", "organization_id", "name", "description", "item_id_type", "user_id_type" })
public class Database extends Base implements Serializable {

	private static final long serialVersionUID = 1261106258660845138L;
	@JsonProperty("id")
	private String id;
	@JsonProperty("organization_id")
	private String organizationId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("item_id_type")
	private String itemIdType;
	@JsonProperty("user_id_type")
	private String userIdType;


// Note that Database class extends of Base class containing the feature to map any XMinds error and additional properties 

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "error" })
public class Base implements Serializable {

	private static final long serialVersionUID = 6984020505060503600L;
	@JsonUnwrapped
	private BaseError error;
	@JsonIgnore
	protected transient Map<String, Object> additionalProperties = new HashMap<>();
```

#### Example of automatic mapping 

```java

// JSON to Java object
public T jsonToObject(String jsonContent, Class<T> clazz) {
	try {
		return this.readValue(jsonContent, clazz);
	} catch (IOException ioe) {
		throw new CompletionException(ioe);
	}
}

// Java object to JSON string
public String objectToJson(Object obj) {
	try {
		return this.writeValueAsString(obj);
	} catch (JsonProcessingException e) {
		throw new CompletionException(e);
	}
}
```

To ensure the use of an automatic refresh token, the Decorator design pattern is implemented adding the ability to obtain a new token from the refresh token stored in memory.

#### Example of decorated method 

```java
// The simple method implementation
public Organization listAllAccounts() throws XmindsException {
	try {
		var jsonStr = this.request.get(getURI(Constants.ENDPOINT_LIST_ALL_ACCOUNTS));
		return (Organization) Parser.parseResponse(organizationMapper.jsonToObject(jsonStr, Organization.class));
	} catch (IOException | InterruptedException ex) {
		throw new ServerException(ex, Constants.UNKNOWN_ERROR_MSG, "0", "500", 0);
	}
}

// The decorated method implementation
public Organization listAllAccounts() throws XmindsException {
	try {
        // Try to execute the method
		return endpoint.listAllAccounts();
	} catch (JwtTokenExpiredException ex) {
        // If a JwtTokenExpiredException ocurred
        // a new token is obtained from refresh token
		endpoint.loginRefreshToken();
        // Then execute method with a valid jwt token
		return endpoint.listAllAccounts();
	}
}

```
