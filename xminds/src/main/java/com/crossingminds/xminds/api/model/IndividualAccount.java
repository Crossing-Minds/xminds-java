package com.crossingminds.xminds.api.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "id", "organization_id", "first_name", "last_name", "email", "password", "role", "verified",
		"db_id", "frontend_user_id" })
public class IndividualAccount extends Base implements Serializable {

	@JsonProperty("id")
	private String id;
	@JsonProperty("organization_id")
	private String organizationId;
	@JsonProperty("first_name")
	private String firstName;
	@JsonProperty("last_name")
	private String lastName;
	@JsonProperty("email")
	private String email;
	@JsonProperty("password")
	private String password;
	@JsonProperty("role")
	private String role;
	@JsonProperty("verified")
	private boolean verified;
	@JsonProperty("db_id")
	private String dbId;
	@JsonProperty("frontend_user_id")
	private String frontendUserId;
	private static final long serialVersionUID = 5967437521568118480L;

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public IndividualAccount() {
	}

	/**
	 *
	 * @param organizationId
	 * @param firstName
	 * @param lastName
	 * @param password
	 * @param frontendUserId
	 * @param role
	 * @param dbId
	 * @param verified
	 * @param id
	 * @param email
	 */
	public IndividualAccount(String id, String organizationId, String firstName, String lastName, String email,
			String password, String role, boolean verified, String dbId, String frontendUserId) {
		super();
		this.id = id;
		this.organizationId = organizationId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.role = role;
		this.verified = verified;
		this.dbId = dbId;
		this.frontendUserId = frontendUserId;
	}

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("organization_id")
	public String getOrganizationId() {
		return organizationId;
	}

	@JsonProperty("organization_id")
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	@JsonProperty("first_name")
	public String getFirstName() {
		return firstName;
	}

	@JsonProperty("first_name")
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@JsonProperty("last_name")
	public String getLastName() {
		return lastName;
	}

	@JsonProperty("last_name")
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@JsonProperty("email")
	public String getEmail() {
		return email;
	}

	@JsonProperty("email")
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty("password")
	public String getPassword() {
		return password;
	}

	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonProperty("role")
	public String getRole() {
		return role;
	}

	@JsonProperty("role")
	public void setRole(String role) {
		this.role = role;
	}

	@JsonProperty("verified")
	public boolean isVerified() {
		return verified;
	}

	@JsonProperty("verified")
	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	@JsonProperty("db_id")
	public String getDbId() {
		return dbId;
	}

	@JsonProperty("db_id")
	public void setDbId(String dbId) {
		this.dbId = dbId;
	}

	@JsonProperty("frontend_user_id")
	public String getFrontendUserId() {
		return frontendUserId;
	}

	@JsonProperty("frontend_user_id")
	public void setFrontendUserId(String frontendUserId) {
		this.frontendUserId = frontendUserId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).append("organizationId", organizationId)
				.append("firstName", firstName).append("lastName", lastName).append("email", email)
				.append("password", password).append("role", role).append("verified", verified).append("dbId", dbId)
				.append("frontendUserId", frontendUserId).append("additionalProperties", additionalProperties)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(organizationId).append(firstName).append(lastName).append(password)
				.append(frontendUserId).append(role).append(dbId).append(verified).append(id)
				.append(additionalProperties).append(email).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof IndividualAccount)) {
			return false;
		}
		IndividualAccount rhs = ((IndividualAccount) other);
		return new EqualsBuilder().append(organizationId, rhs.organizationId).append(firstName, rhs.firstName)
				.append(lastName, rhs.lastName).append(password, rhs.password)
				.append(frontendUserId, rhs.frontendUserId).append(role, rhs.role).append(dbId, rhs.dbId)
				.append(verified, rhs.verified).append(id, rhs.id)
				.append(additionalProperties, rhs.additionalProperties).append(email, rhs.email).isEquals();
	}

}