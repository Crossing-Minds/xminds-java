package com.crossingminds.api.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Getter
@Setter
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
	private Map<String, Object> errorData;
	@Builder.Default
	@JsonIgnore
	protected transient Map<String, Object> additionalProperties = new HashMap<>();
	private static final long serialVersionUID = 8090043875893875131L;

}
