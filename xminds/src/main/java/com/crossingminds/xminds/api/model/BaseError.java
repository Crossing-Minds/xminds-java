package com.crossingminds.xminds.api.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "error_code", "error_name", "message", "error_data" })
public class BaseError implements Serializable {

	@JsonProperty("error_code")
	private int errorCode;
	@JsonProperty("error_name")
	private String errorName;
	@JsonProperty("message")
	private String message;
	@JsonProperty("error_data")
	private ErrorData errorData;
	@JsonIgnore
	protected transient Map<String, Object> additionalProperties = new HashMap<>();
	private static final long serialVersionUID = 8090043875893875131L;

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public BaseError() {
	}

	/**
	 *
	 * @param errorName
	 * @param errorCode
	 * @param message
	 * @param errorData
	 */
	public BaseError(int errorCode, String errorName, String message, ErrorData errorData) {
		super();
		this.errorCode = errorCode;
		this.errorName = errorName;
		this.message = message;
		this.errorData = errorData;
	}

	@JsonProperty("error_code")
	public int getErrorCode() {
		return errorCode;
	}

	@JsonProperty("error_code")
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@JsonProperty("error_name")
	public String getErrorName() {
		return errorName;
	}

	@JsonProperty("error_name")
	public void setErrorName(String errorName) {
		this.errorName = errorName;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	@JsonProperty("error_data")
	public ErrorData getErrorData() {
		return errorData;
	}

	@JsonProperty("error_data")
	public void setErrorData(ErrorData errorData) {
		this.errorData = errorData;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("errorCode", errorCode).append("errorName", errorName)
				.append("message", message).append("errorData", errorData)
				.toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(errorCode).append(errorName).append(message)
				.append(errorData).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof BaseError)) {
			return false;
		}
		BaseError rhs = ((BaseError) other);
		return new EqualsBuilder().append(errorCode, rhs.errorCode).append(errorName, rhs.errorName)
				.append(message, rhs.message)
				.append(errorData, rhs.errorData).isEquals();
	}

}