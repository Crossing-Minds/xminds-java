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
	@Builder.Default
	@JsonIgnore
	protected transient Map<String, Object> additionalProperties = new HashMap<>();
	private static final long serialVersionUID = -7315497296996355910L;

}
