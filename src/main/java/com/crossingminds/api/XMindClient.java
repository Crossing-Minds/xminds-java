package com.crossingminds.api;

import java.io.Serializable;
import java.util.List;

import com.crossingminds.api.exception.AuthenticationException;
import com.crossingminds.api.exception.DuplicatedException;
import com.crossingminds.api.exception.NotFoundException;
import com.crossingminds.api.exception.RefreshTokenExpiredException;
import com.crossingminds.api.exception.XMindException;
import com.crossingminds.api.model.Database;
import com.crossingminds.api.model.Filter;
import com.crossingminds.api.model.IndividualAccount;
import com.crossingminds.api.model.Item;
import com.crossingminds.api.model.Property;
import com.crossingminds.api.model.RootAccount;
import com.crossingminds.api.model.ServiceAccount;
import com.crossingminds.api.model.Token;
import com.crossingminds.api.model.User;
import com.crossingminds.api.model.UserInteraction;
import com.crossingminds.api.model.UserRating;
import com.crossingminds.api.response.AccountList;
import com.crossingminds.api.response.DatabasePage;
import com.crossingminds.api.response.DatabaseStatus;
import com.crossingminds.api.response.ItemBulk;
import com.crossingminds.api.response.PropertyList;
import com.crossingminds.api.response.Recommendation;
import com.crossingminds.api.response.UserBulk;
import com.crossingminds.api.response.UserRatingBulk;
import com.crossingminds.api.response.UserRatingPage;

public interface XMindClient extends Serializable {

	/**
	 * Retrieve all the accounts that belong to the organization of the token.
	 *
	 * @return organization (individualAccounts list and the serviceAccounts list)
	 * @throws XMindException
	 */
	AccountList listAllAccounts() throws XMindException;

	/**
	 * Create a new account for an individual, identified by an email. To create a
	 * new account it is necessary to have generated a token previously (using
	 * loginRoot, for instance with the root account).
	 *
	 * @param individualAccount (firstName, lastName, email, password, role)
	 * @return individualAccount (id)
	 * @throws DuplicatedException
	 */
	IndividualAccount createIndividualAccount(IndividualAccount individualAccount) throws XMindException;

	/**
	 * Delete another individual account by email address that belong to the
	 * organization of the token.
	 *
	 * @param individualAccount (email)
	 * @throws NotFoundException
	 */
	void deleteIndividualAccount(IndividualAccount individualAccount) throws XMindException;

	/**
	 * Login with the root account, without selecting any database. This is useful
	 * to create new databases, or create new accounts.
	 *
	 * @param rootAccount (email, password)
	 * @return Token
	 * @throws AuthenticationException
	 */
	Token loginRoot(RootAccount rootAccount) throws XMindException;

	/**
	 * Login on a database with your account, using a refresh token. A new JWT token
	 * and a (potentially new) refresh_token will be created.
	 *
	 * @return Token
	 * @throws NotFoundException
	 * @throws AuthenticationException
	 * @throws RefreshTokenExpiredException
	 */
	Token loginRefreshToken() throws XMindException;

	/**
	 * Create a new service account, identified by a service name. To create a new
	 * account it is necessary to have generated a token previously (using
	 * loginRoot, for instance with the root account).
	 *
	 * @param serviceAccount (name, password, role)
	 * @return serviceAccount (id)
	 * @throws DuplicatedException
	 */
	ServiceAccount createServiceAccount(ServiceAccount serviceAccount) throws XMindException;

	/**
	 * Delete another service account by name that belong to the organization of the
	 * token.
	 *
	 * @param serviceAccount (email)
	 * @throws NotFoundException
	 */
	void deleteServiceAccount(ServiceAccount serviceAccount) throws XMindException;

	/**
	 * Login on a database with your account, using your email and password
	 * combination.
	 *
	 * @param individualAccount (email, password, dbId)
	 * @return Token
	 * @throws AuthenticationException
	 */
	Token loginIndividual(IndividualAccount individualAccount) throws XMindException;

	/**
	 * Login on a database with a service account, using a service name and password
	 * combination.
	 *
	 * @param serviceAccount (name, password, dbId, frontendUserId)
	 * @return Token
	 * @throws AuthenticationException
	 */
	Token loginService(ServiceAccount serviceAccount) throws XMindException;

	/**
	 * Useful to send a new verification code to the email address of an individual
	 * account.
	 *
	 * @param email
	 * @throws XMindException
	 * @throws AuthenticationException
	 */
	void resendVerificationCode(String email) throws XMindException;

	/**
	 * Useful to verify the email of an individual account. You can't use an
	 * individual account without verifying the email. If you didn't receive our
	 * email, please use resendVerificationCode
	 *
	 * @param code
	 * @param email
	 * @throws NotFoundException
	 * @throws AuthenticationException
	 */
	void verifyAccount(String code, String email) throws XMindException;

	/**
	 * Delete the account you're logged to with your current token.
	 *
	 * @throws XMindException
	 */
	void deleteCurrentAccount() throws XMindException;

	/**
	 * Create a new database.
	 *
	 * @param database (name, description, itemIdType, userIdType)
	 * @return database (id)
	 * @throws XMindException
	 */
	Database createDatabase(Database database) throws XMindException;

	/**
	 * Retrieve all databases for the organization you�re logged to with your current token. 
	 * The result is paginated.
	 * 
	 * @return DatabasePage
	 * @throws XMindException
	 */
	DatabasePage listAllDatabases() throws XMindException;

	/**
	 * Retrieve the metadata for the database you�re logged to with your current token.
	 * 
	 * @return Database
	 * @throws XMindException
	 */
	Database getCurrentDatabase() throws XMindException;

	/**
	 * Delete the database you�re logged to with your current token. 
	 * Everything will be deleted: all the items, all the users, all the ratings, 
	 * all refresh token and all the machine learning models that were created. 
	 * Note that you cannot undo this operation.
	 * 
	 * @throws XMindException
	 */
	void deleteCurrentDatabase() throws XMindException;

	/**
	 * Retrieve status of database. Initially the database will be in �pending� status. 
	 * Until the status switch to �ready� you will not be able to get recommendations.
	 * 
	 * @return DatabaseStatus (status)
	 * @throws XMindException
	 */
	DatabaseStatus getCurrentDatabaseStatus() throws XMindException;

	/**
	 * Retrieve all user-properties for the current database.
	 *
	 * @return User (properties list)
	 * @throws XMindException
	 */
	PropertyList listAllUserProperties() throws XMindException;

	/**
	 * Create a new user-property, identified by property_name (case-insensitive).
	 *
	 * @param Property (propertyName, valueType, repeated)
	 * @throws XMindException
	 */
	void createUserProperty(Property userProperty) throws XMindException;

	/**
	 * Get one user-property given its property_name.
	 *
	 * @param propertyName
	 * @throws XMindException
	 */
	Property getUserProperty(String propertyName) throws XMindException;

	/**
	 * Delete an user-property given by its name
	 *
	 * @param propertyName
	 * @throws XMindException
	 */
	void deleteUserProperty(String propertyName) throws XMindException;

	/**
	 * Get one user given its ID.
	 *
	 * @param userId
	 * @return User
	 * @throws XMindException
	 */
	User getUser(Object userId) throws XMindException;

	/**
	 * Create a new user, or update it if the user_id already exists.
	 *
	 * @param User
	 * @throws XMindException
	 */
	void createOrUpdateUser(User user) throws XMindException;

	/**
	 * Create many users in bulk, or update the ones for which the user_id already exist.
	 *
	 * @param users - the users to be created/updated
	 * @param chunkSize - to split the requests in chunks of this size (default: 1K)
	 * @param waitForCompletion - to use the default value set this as null
	 * @throws XMindException
	 */
	void createOrUpdateUsersBulk(List<User> users, Integer chunkSize, Boolean waitForCompletion) throws XMindException;

	/**
	 * Get multiple users by page.
	 * The response is paginated, you can control the response amount
	 * and offset using the query parameters amt and cursor.
	 *
	 * @return UserBulk
	 * @throws XMindException
	 */
	UserBulk listUsersPaginated(Integer amt, String cursor) throws XMindException;

	/**
	 * Get multiple users given their IDs.
	 *
	 * @param usersId (list)
	 * @return List<User>
	 * @throws XMindException
	 */
	List<User> listUsers(List<Object> usersId) throws XMindException;

	/**
	 * Partially update some properties of an user. 
	 * The properties that are not listed in the request body will be left unchanged. 
	 * The list of values given for repeated properties will replace (not append) the previous list of values.
	 *
	 * @param user - The user properties to update
	 * @param createIfMissing - to control whether an error should be returned or a new user should be 
	 * 					created when the user_id does not already exist
	 * @throws XMindException
	 */
	void partialUpdateUser(User user, boolean createIfMissing) throws XMindException;

	/**
	 * Partially update some properties of many users. 
	 * The properties that are not listed in the request body will be left unchanged. 
	 * The list of values given for repeated properties will replace (not append) the previous list of values.
	 *
	 * @param users - the users to be created/updated
	 * @param chunkSize - to split the requests in chunks of this size (default: 1K)
	 * @param waitForCompletion - Set to true to wait for completion and respond with 200 OK. 
	 * 							Set to false to respond immediately with 202 Accepted. 
	 * 							To use the default value set to null
	 * @param createIfMissing - to control whether an error should be returned or a new users should be 
	 * 					created when the user_id does not already exist
	 * @throws XMindException
	 */
	void partialUpdateUsersBulk(List<User> users, Integer chunkSize, Boolean waitForCompletion, boolean createIfMissing) throws XMindException;

	/**
	 * Retrieve all item-properties for the current database.
	 *
	 * @return PropertyList
	 * @throws XMindException
	 */
	PropertyList listAllItemProperties() throws XMindException;

	/**
	 * Create a new item-property, identified by property_name (case-insensitive).
	 *
	 * @param Property (propertyName, valueType, repeated)
	 * @throws XMindException
	 */
	void createItemProperty(Property itemProperty) throws XMindException;

	/**
	 * Get one item-property given its property_name.
	 *
	 * @param propertyName
	 * @throws XMindException
	 */
	Property getItemProperty(String propertyName) throws XMindException;

	/**
	 * Delete an item-property given by its name
	 *
	 * @param propertyName
	 * @throws XMindException
	 */
	void deleteItemProperty(String propertyName) throws XMindException;

	/**
	 * Get one item given its ID.
	 *
	 * @param itemId
	 * @throws XMindException
	 */
	Item getItem(Object itemId) throws XMindException;

	/**
	 * Create a new item, or update it if the item_id already exists.
	 *
	 * @param Item
	 * @throws XMindException
	 */
	void createOrUpdateItem(Item item) throws XMindException;

	/**
	 * Create many items in bulk, or update the ones for which the item_id already exist.
	 *
	 * @param items - the items to create/update
	 * @param chunkSize - default=1000
	 * @param waitForCompletion - to use the default value set this as null
	 * @throws XMindException
	 */
	void createOrUpdateItemsBulk(List<Item> items, Integer chunkSize, Boolean waitForCompletion) throws XMindException;

	/**
	 * Get multiple items by page.
	 * The response is paginated, you can control the response amount
	 * and offset using the query parameters amt and cursor.
	 *
	 * @return ItemBulk
	 * @throws XMindException
	 */
	ItemBulk listItemsPaginated(Integer amt, String cursor) throws XMindException;

	/**
	 * Get multiple items given their IDs.
	 *
	 * @param itemsId (list)
	 * @return List<Item>
	 * @throws XMindException
	 */
	List<Item> listItems(List<Object> itemsId) throws XMindException;

	/**
	 * Partially update some properties of an item. 
	 * The properties that are not listed in the request body will be left unchanged. 
	 * The list of values given for repeated properties will replace (not append) the previous list of values.
	 *
	 * @param item - The item properties to update
	 * @param createIfMissing - to control whether an error should be returned or a new item should be 
	 * 					created when the item_id does not already exist
	 * @throws XMindException
	 */
	void partialUpdateItem(Item item, boolean createIfMissing) throws XMindException;

	/**
	 * Partially update some properties of many items. 
	 * The properties that are not listed in the request body will be left unchanged. 
	 * The list of values given for repeated properties will replace (not append) the previous list of values.
	 *
	 * @param items - the items to create/update
	 * @param chunkSize - default=1000
	 * @param waitForCompletion - Set to true to wait for completion and respond with 200 OK. 
	 * 							Set to false to respond immediately with 202 Accepted. 
	 * 							To use the default value set to null
	 * @param createIfMissing - to control whether an error should be returned or a new items should be 
	 * 					created when the item_id does not already exist
	 * @throws XMindException
	 */
	void partialUpdateItemsBulk(List<Item> items, Integer chunkSize, Boolean waitForCompletion, boolean createIfMissing) throws XMindException;

	/**
	 * Create or update a rating for a user and an item. If the rating exists for the tuple 
	 * (userId, item_id) then it is updated, otherwise it is created. 
	 * 
	 * @param userId
	 * @param rating
	 * @throws XMindException
	 */
	void createOrUpdateRating(Object userId, UserRating rating) throws XMindException;

	/**
	 * Delete a single rating for a given user.
	 * 
	 * @param userId
	 * @param itemId
	 * @throws XMindException
	 */
	void deleteRating(Object userId, Object itemId) throws XMindException;

	/**
	 * List the ratings of one user. The response is paginated, you can control the response 
	 * amount and offset using the query parameters amt and page.
	 * 
	 * @param userId
	 * @param page � Optional. [min: 1] Page to be listed.
	 * @param amt - Optional. [min: 1 max: 64] Amount of ratings to return 
	 * @return UserRatingPage
	 * @throws XMindException
	 */
	UserRatingPage listUserRatings(Object userId, Integer page, Integer amt) throws XMindException;

	/**
	 * Create or update bulks of ratings for a single user and many items.
	 * 
	 * @param userId
	 * @param ratings - UserRating list
	 * @throws XMindException
	 */
	void createOrUpdateOneUserRatingsBulk(Object userId, List<UserRating> ratings, Integer chunkSize) throws XMindException;

	/**
	 * Create or update large bulks of ratings for many users and many items.
	 * 
	 * @param ratings - UserRating List
	 * @throws XMindException
	 */
	void createOrUpdateRatingsBulk(List<UserRating> userRatings, Integer chunkSize) throws XMindException;

	/**
	 * List the ratings of one database. 
	 * 
	 * The response is paginated, you can control the response 
	 * amount and offset using the query parameters amt and cursor.
	 * 
	 * @param amt
	 * @param cursor
	 * @return UserRatingBulk
	 * @throws XMindException
	 */
	UserRatingBulk listRatings(Integer amt, String cursor) throws XMindException;

	/**
	 * Get similar items.
	 * 
	 * @param itemId
	 * @param amt
	 * @param cursor
	 * @param filters
	 * @return Recommendation
	 * @throws XMindException
	 */
	Recommendation getRecommendationsItemToItems(Object itemId, Integer amt, String cursor, List<Filter> filters) throws XMindException;

	/**
	 * Get items recommendations given the ratings of an anonymous session.
	 * 
	 * @param ratings
	 * @param userProperties
	 * @param amt
	 * @param cursor
	 * @param filters
	 * @param excludeRatedItems
	 * @return Recommendation
	 * @throws XMindException
	 */
	Recommendation getRecommendationsSessionToItems(List<UserRating> ratings, User userProperties, Integer amt, String cursor, List<Filter> filters, boolean excludeRatedItems) throws XMindException;

	/**
	 * Get items recommendations given a user ID.
	 * 
	 * @param userId
	 * @param amt
	 * @param cursor
	 * @param filters
	 * @param excludeRatedItems
	 * @return Recommendation
	 * @throws XMindException
	 */
	Recommendation getRecommendationsUserToItems(Object userId, Integer amt, String cursor, List<Filter> filters, boolean excludeRatedItems) throws XMindException;

	/**
	 * Create a new interaction for a user and an item.
	 * An inferred rating will be created or updated for the tuple (user_id, item_id).
	 * 
	 * @param interaction
	 * @throws XMindException
	 */
	void createInteraction(UserInteraction interaction) throws XMindException;

	/**
	 * Create or update large bulks of interactions for many users and many items.
	 * Inferred ratings will be created or updated for all tuples (user_id, item_id)
	 * 
	 * @param userInteractions - UserInteraction List
	 * @throws XMindException
	 */
	void createInteractionsBulk(List<UserInteraction> userInteractions, Integer chunkSize) throws XMindException;

}
