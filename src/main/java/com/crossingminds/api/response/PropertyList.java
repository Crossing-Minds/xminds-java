package com.crossingminds.api.response;

import java.io.Serializable;
import java.util.List;

import com.crossingminds.api.model.Property;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

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
public class PropertyList implements Serializable {

	private static final long serialVersionUID = 1248912942656066549L;
	@JsonProperty("properties")
	private List<Property> properties;

}
