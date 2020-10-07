package com.crossingminds.xminds.api.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "id", "organization_id", "name", "password", "role", "db_id", "frontend_user_id" })
public class ServiceAccount extends Base implements Serializable {

	@JsonProperty("id")
	private String id;
	@JsonProperty("organization_id")
	private String organizationId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("password")
	private String password;
	@JsonProperty("role")
	private String role;
	@JsonProperty("db_id")
	private String dbId;
	@JsonProperty("frontend_user_id")
	private String frontendUserId;

	private static final long serialVersionUID = 205003207955071228L;

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public ServiceAccount() {
	}

	/**
	 *
	 * @param organizationId
	 * @param password
	 * @param frontendUserId
	 * @param role
	 * @param dbId
	 * @param name
	 * @param id
	 */
	public ServiceAccount(String id, String organizationId, String name, String password, String role, String dbId,
			String frontendUserId) {
		super();
		this.id = id;
		this.organizationId = organizationId;
		this.name = name;
		this.password = password;
		this.role = role;
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

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
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
		return new ToStringBuilder(this).append("id", id).append("organizationId", organizationId).append("name", name)
				.append("password", password).append("role", role).append("dbId", dbId)
				.append("frontendUserId", frontendUserId).append("additionalProperties", additionalProperties)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(organizationId).append(password).append(frontendUserId).append(role)
				.append(dbId).append(name).append(id).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof ServiceAccount)) {
			return false;
		}
		ServiceAccount rhs = ((ServiceAccount) other);
		return new EqualsBuilder().append(organizationId, rhs.organizationId).append(password, rhs.password)
				.append(frontendUserId, rhs.frontendUserId).append(role, rhs.role).append(dbId, rhs.dbId)
				.append(name, rhs.name).append(id, rhs.id).append(additionalProperties, rhs.additionalProperties)
				.isEquals();
	}

}