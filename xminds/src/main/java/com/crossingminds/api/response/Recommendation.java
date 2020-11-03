package com.crossingminds.api.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties({"amt", "has_next"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "items_id" })
public class Recommendation extends Bulk {

	@JsonProperty("items_id")
	private List<String> itemsId;
	private static final long serialVersionUID = 3572449125939981167L;

}
