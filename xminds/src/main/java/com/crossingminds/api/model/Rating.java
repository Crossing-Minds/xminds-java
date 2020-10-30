package com.crossingminds.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "item_id", "rating", "timestamp" })
public class Rating implements Serializable {

	@JsonProperty("item_id")
	private Object itemId;
	@JsonProperty("rating")
	private float rating;
	@JsonProperty("timestamp")
	private long timestamp;
	private static final long serialVersionUID = 3463959195071574750L;

}
