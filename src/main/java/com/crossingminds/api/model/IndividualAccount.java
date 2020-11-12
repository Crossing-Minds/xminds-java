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
@JsonPropertyOrder({ "id", "organization_id", "first_name", "last_name", "email", "password", "role", "verified",
		"db_id", "frontend_user_id" })
public class IndividualAccount extends Base {

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

}