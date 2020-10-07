package com.crossingminds.xminds.api.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "jwtToken", "refresh_token", "database" })
public class Token extends Base implements Serializable {

	@JsonProperty("token")
	private String jwtToken;
	@JsonProperty("refresh_token")
	private String refreshToken;
	@JsonProperty("database")
	private Database database;

	private static final long serialVersionUID = -1861609617818872540L;

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public Token() {
	}

	/**
	 *
	 * @param database
	 * @param token
	 * @param refreshToken
	 */
	public Token(String token, String refreshToken, Database database) {
		super();
		this.jwtToken = token;
		this.refreshToken = refreshToken;
		this.database = database;
	}

	@JsonProperty("jwtToken")
	public String getToken() {
		return jwtToken;
	}

	@JsonProperty("jwtToken")
	public void setToken(String token) {
		this.jwtToken = token;
	}

	@JsonProperty("refresh_token")
	public String getRefreshToken() {
		return refreshToken;
	}

	@JsonProperty("refresh_token")
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@JsonProperty("database")
	public Database getDatabase() {
		return database;
	}

	@JsonProperty("database")
	public void setDatabase(Database database) {
		this.database = database;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("jwtToken", jwtToken).append("refreshToken", refreshToken)
				.append("database", database).append("additionalProperties", additionalProperties).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(database).append(additionalProperties).append(jwtToken).append(refreshToken)
				.toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof Token)) {
			return false;
		}
		Token rhs = ((Token) other);
		return new EqualsBuilder().append(database, rhs.database).append(additionalProperties, rhs.additionalProperties)
				.append(jwtToken, rhs.jwtToken).append(refreshToken, rhs.refreshToken).isEquals();
	}

}