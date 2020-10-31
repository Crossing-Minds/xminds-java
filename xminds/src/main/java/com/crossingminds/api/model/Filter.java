package com.crossingminds.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "property_name", "op", "value" })
public class Filter implements Serializable {

	@JsonProperty("property_name")
	private String propertyName;
	@JsonProperty("op")
	private String op;
	@JsonProperty("value")
	private Object value;
	private static final long serialVersionUID = -2995454381086489606L;

	public String toString() {
		return (this.propertyName + ":" + this.op + (this.value != null ? ":" + this.value : "")).replace("[", "")
				.replace("]", "").replace(", ", ",");
	}

}
