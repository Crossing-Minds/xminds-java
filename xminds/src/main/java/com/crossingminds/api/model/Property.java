package com.crossingminds.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "property_name", "value_type", "repeated" })
public class Property extends Base implements Serializable {

	@JsonProperty("property_name")
	public String propertyName;
	@JsonProperty("value_type")
	public String valueType;
	@JsonProperty("repeated")
	public boolean repeated;
	private final static long serialVersionUID = -3837092897825646247L;

}