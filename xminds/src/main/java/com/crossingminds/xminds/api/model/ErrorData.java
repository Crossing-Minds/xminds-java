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
@JsonPropertyOrder({ "error", "type", "key", "name", "method" })
public class ErrorData implements Serializable {

	@JsonProperty("error")
	private String error;
	@JsonProperty("type")
	private String type;
	@JsonProperty("key")
	private String key;
	@JsonProperty("name")
	private String name;
	@JsonProperty("method")
	private String method;
	@JsonIgnore
	protected transient Map<String, Object> additionalProperties = new HashMap<>();
	private static final long serialVersionUID = -7315497296996355910L;

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public ErrorData() {
	}

	/**
	 *
	 * @param method
	 * @param name
	 * @param error
	 * @param type
	 * @param key
	 */
	public ErrorData(String error, String type, String key, String name, String method) {
		super();
		this.error = error;
		this.type = type;
		this.key = key;
		this.name = name;
		this.method = method;
	}

	@JsonProperty("error")
	public String getError() {
		return error;
	}

	@JsonProperty("error")
	public void setError(String error) {
		this.error = error;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("key")
	public String getKey() {
		return key;
	}

	@JsonProperty("key")
	public void setKey(String key) {
		this.key = key;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("method")
	public String getMethod() {
		return method;
	}

	@JsonProperty("method")
	public void setMethod(String method) {
		this.method = method;
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
		return new ToStringBuilder(this).append("error", error).append("type", type).append("key", key)
				.append("name", name).append("method", method).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(method).append(name).append(error).append(type).append(key).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if (!(other instanceof ErrorData)) {
			return false;
		}
		ErrorData rhs = ((ErrorData) other);
		return new EqualsBuilder().append(method, rhs.method).append(name, rhs.name).append(error, rhs.error)
				.append(type, rhs.type).append(key, rhs.key).isEquals();
	}

}