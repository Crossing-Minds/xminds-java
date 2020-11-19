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
@JsonPropertyOrder({ "jwtToken", "refresh_token", "database" })
public class Token extends Base {

	@JsonProperty("token")
	private String jwtToken;
	@JsonProperty("refresh_token")
	private String refreshToken;
	@JsonProperty("database")
	private Database database;
	private static final long serialVersionUID = -1861609617818872540L;

}