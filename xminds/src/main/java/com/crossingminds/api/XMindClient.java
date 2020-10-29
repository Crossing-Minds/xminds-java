package com.crossingminds.api;

import java.util.List;

import com.crossingminds.api.exception.AuthenticationException;
import com.crossingminds.api.exception.DuplicatedException;
import com.crossingminds.api.exception.NotFoundException;
import com.crossingminds.api.exception.RefreshTokenExpiredException;
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
import com.crossingminds.api.response.PropertyList;
import com.crossingminds.api.response.UserBulk;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface XMindClient {

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
	 * @throws JsonProcessingException
	 * @throws XMindException
	 * @throws AuthenticationException
	 */
	void resendVerificationCode(String email) throws XMindException, JsonProcessingException;

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
	 * @throws XMindException
	 */
	User getUser(Object userId) throws XMindException;

	/**
	 * Create a new user, or update it if the user_id already exists.
	 *
	 * @param User
	 * @throws XMindException
	 */
	void createOrUpdateUser(User user)  throws XMindException;

	/**
	 * Create many users in bulk, or update the ones for which the user_id already exist.
	 *
	 * @param users values
	 * @param chunkSize to split the requests in chunks of this size (default: 1K)
	 * @throws XMindException
	 */
	void createOrUpdateUsersBulk(List<User> users, Integer chunkSize) throws XMindException;

	/**
	 * Get multiple users by page.
	 * The response is paginated, you can control the response amount
	 * and offset using the query parameters amt and cursor.
	 *
	 * @return UserBulk
	 * @throws XMindException
	 */
	UserBulk listUsersPaginated(int amt, String cursor) throws XMindException;

	/**
	 * Get multiple users given their IDs.
	 *
	 * @param usersId (list)
	 * @return List<User>
	 * @throws XMindException
	 */
	List<User> listUsers(List<Object> usersId) throws XMindException;

}
