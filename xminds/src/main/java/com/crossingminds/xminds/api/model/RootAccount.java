package com.crossingminds.xminds.api.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "email", "password" })
public class RootAccount extends Base implements Serializable {

	@JsonProperty("email")
	private String email;
	@JsonProperty("password")
	private String password;
	private final static long serialVersionUID = -6170164486974115234L;

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public RootAccount() {
	}

	/**
	 *
	 * @param password
	 * @param email
	 */
	public RootAccount(String email, String password) {
		super();
		this.email = email;
		this.password = password;
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

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("email", email).append("password", password)
				.append("additionalProperties", additionalProperties).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(password).append(additionalProperties).append(email).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof RootAccount) == false) {
			return false;
		}
		RootAccount rhs = ((RootAccount) other);
		return new EqualsBuilder().append(password, rhs.password).append(additionalProperties, rhs.additionalProperties)
				.append(email, rhs.email).isEquals();
	}

}