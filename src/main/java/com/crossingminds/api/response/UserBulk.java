package com.crossingminds.api.response;

import java.io.Serializable;
import java.util.List;

import com.crossingminds.api.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"users_m2m", "amt"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "users"})
public class UserBulk extends Bulk implements Serializable {

	@JsonProperty("users")
	private List<User> users;
	private static final long serialVersionUID = -6521647095678844299L;

}
