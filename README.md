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
mvn clean test
```

## Design considerations

The API is built as a Java / Maven project.

For JSON mapping we use the Jackson standard (Java JSON library) (see information [here](https://github.com/FasterXML/jackson)) which is a powerful set of data processing tools for Java (and the JVM platform) so it is well known and used.

To comply with the Jackson standard it is necessary to generate model classes (POJOs) to map each entity of the system with its attributes, but with the ability to map extra attributes through an additional attribute for this purpose.

#### Example of a POJO

```java
// Database class to map Database entity

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "id", "organization_id", "name", "description", "item_id_type", "user_id_type", "counters" })
public class Database extends Base {

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
	@JsonProperty("counters")
	private Counters counters;
	private static final long serialVersionUID = 1261106258660845138L;

// Note that Database class extends of Base class containing the feature to map any XMinds error and additional properties 

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "error" })
public class Base implements Serializable {

	@JsonUnwrapped
	@JsonProperty("error")
	@JsonIgnore
	private BaseError error;
	@Builder.Default
	@JsonIgnore
	protected transient Map<String, Object> additionalProperties = new HashMap<>();
	private static final long serialVersionUID = 6984020505060503600L;

```
Also [@Lombok](https://projectlombok.org/) is used to simplify the POJO construction.


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

## Authentication

To ensure the use of an automatic refresh token, the Decorator design pattern is implemented (through reflection) adding the ability to obtain a new token from the refresh token stored in memory.

#### Example of implementation

```java
@LoginRequired
public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	try {
		return method.invoke(xmindClient, args);
	} catch (InvocationTargetException e) {
		try {
			throw e.getTargetException();
		} catch (JwtTokenExpiredException ex) {
			if (!this.hasLoginRequired(method))
				throw ex;
			xmindClient.loginRefreshToken();
			return method.invoke(xmindClient, args);
		}
	}
}

```
All methods annotated with @LoginRequired are intercepted and then invoked through this flow to ensure authentication using the refresh token.

#### Creating instances

```java
// Building a default client
XMindClient client = new XmindBuilder().build();
client.loginService(aServiceAccount);

// It is possible to provide some optional parameters
XMindClient client = new XmindBuilder()
	.withHost("https://staging-api.crossingminds.com/") // Example for staging host
	.withServiceAccount(aServiceAccount) // Will do the login service implicitly
	.withUserAgent("USER_AGENT") // Used to keep track of requests (example: "Shopify/MyOwnStoreName")
	.withHttpClient(mockHttpClient) // For testing purposes only 
	.build();
```

#### Create a Custom client (extending XmindClient)
```java
// Example of a CustomXmindsClient interface
public interface CustomXmindsClient extends XMindClient {

	MyObject myCustomMethod(String id) throws XMindException;

}

// Example of a CustomXmindsClient implementation
public class CustomXmindsClientImpl extends XMindClientImpl implements CustomXmindsClient {

	private final String ENDPOINT_RESOURCES = "resource/%s/";

	public CustomXmindsClientImpl(HttpClient httpClient, String host, ServiceAccount serviceAccount, String externalUserAgent) throws XMindException {
		super(httpClient, host, serviceAccount, externalUserAgent);
	}

	@LoginRequired
	public MyObject myCustomMethod(String id) throws XMindException {
		var uri = String.format(ENDPOINT_RESOURCES, id);
		return super.request.get(uri, MyObject.class);
	}

}

// Building instances
var builder = new XmindBuilder();

// Example of how to create a default instance of the custom client
XMindClient client = (CustomXmindsClient) builder.build(CustomXmindsClient.class, CustomXmindsClientImpl.class);

// Example of creating an instance with custom client parameters (optional)
XMindClient client = (CustomXmindsClient) builder
	.withHost("https://staging-api.crossingminds.com/") // Example for staging host
	.withServiceAccount(aServiceAccount) // Will do the login service implicitly
	.withUserAgent("USER_AGENT") // Used to keep track of requests (example: "Shopify/MyOwnStoreName")
	.withHttpClient(mockHttpClient) // For testing purposes only 
	.build(CustomXmindsClient.class, CustomXmindsClientImpl.class);

```
