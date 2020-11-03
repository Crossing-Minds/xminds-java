package com.crossingminds.api.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "user_id", "item_id", "rating", "timestamp" })
public class UserRating implements Serializable {

	@JsonProperty("user_id")
	private Object userId;
	@JsonProperty("item_id")
	private Object itemId;
	@JsonProperty("rating")
	private float rating;
	@JsonProperty("timestamp")
	private long timestamp;
	private static final long serialVersionUID = 4033412927599972749L;

}
