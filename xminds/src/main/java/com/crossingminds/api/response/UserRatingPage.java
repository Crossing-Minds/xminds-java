package com.crossingminds.api.response;

import java.util.List;

import com.crossingminds.api.model.Rating;
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
@JsonIgnoreProperties("amt")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "user_ratings", "has_next" })
public class UserRatingPage extends Page {

	@JsonProperty("user_ratings")
	private List<Rating> userRatings;
	@JsonProperty("has_next")
	private boolean hasNext;
	private static final long serialVersionUID = 2436535301644174245L;

}
