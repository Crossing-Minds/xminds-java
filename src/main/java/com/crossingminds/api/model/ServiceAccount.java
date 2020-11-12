package com.crossingminds.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "id", "organization_id", "name", "password", "role", "db_id", "frontend_user_id" })
public class ServiceAccount extends Base {

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

}