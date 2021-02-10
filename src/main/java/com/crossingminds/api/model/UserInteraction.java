package com.crossingminds.api.model;

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
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "user_id", "item_id", "interaction_type", "timestamp" })
public class UserInteraction extends Base {

	@JsonProperty("user_id")
	private transient Object userId;
	@JsonProperty("item_id")
	private transient Object itemId;
	@JsonProperty("interaction_type")
	private String interactionType;
	@JsonProperty("timestamp")
	private double timestamp;
	private static final long serialVersionUID = 4033412927599972749L;

}
