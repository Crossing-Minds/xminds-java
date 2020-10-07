package com.crossingminds.xminds.api.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "id", "organization_id", "name", "description", "item_id_type", "user_id_type" })
public class Database extends Base implements Serializable {

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
	private static final long serialVersionUID = 1261106258660845138L;

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public Database() {
	}

	/**
	 *
	 * @param organizationId
	 * @param itemIdType
	 * @param userIdType
	 * @param name
	 * @param description
	 * @param id
	 */
	public Database(String id, String organizationId, String name, String description, String itemIdType,
			String userIdType) {
		super();
		this.id = id;
		this.organizationId = organizationId;
		this.name = name;
		this.description = description;
		this.itemIdType = itemIdType;
		this.userIdType = userIdType;
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

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("item_id_type")
	public String getItemIdType() {
		return itemIdType;
	}

	@JsonProperty("item_id_type")
	public void setItemIdType(String itemIdType) {
		this.itemIdType = itemIdType;
	}

	@JsonProperty("user_id_type")
	public String getUserIdType() {
		return userIdType;
	}

	@JsonProperty("user_id_type")
	public void setUserIdType(String userIdType) {
		this.userIdType = userIdType;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).append("organizationId", organizationId).append("name", name)
				.append("description", description).append("itemIdType", itemIdType).append("userIdType", userIdType)
				.append("additionalProperties", additionalProperties).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(organizationId).append(itemIdType).append(userIdType).append(name)
				.append(description).append(id).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof Database)) {
			return false;
		}
		Database rhs = ((Database) other);
		return new EqualsBuilder().append(organizationId, rhs.organizationId).append(itemIdType, rhs.itemIdType)
				.append(userIdType, rhs.userIdType).append(name, rhs.name).append(description, rhs.description)
				.append(id, rhs.id).append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}