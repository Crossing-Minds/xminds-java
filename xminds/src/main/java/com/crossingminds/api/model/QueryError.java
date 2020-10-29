package com.crossingminds.api.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "user", "user_id", "non_field_errors" })
class QueryError implements Serializable {

	@JsonProperty("user")
	private List<String> user;
	@JsonProperty("user_id")
	private List<String> userId;
	@JsonProperty("non_field_errors")
	private List<String> nonFieldErrors;
	private static final long serialVersionUID = -8793427512465961418L;
	
}