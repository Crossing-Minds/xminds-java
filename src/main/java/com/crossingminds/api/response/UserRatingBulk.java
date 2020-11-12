package com.crossingminds.api.response;

import java.io.Serializable;
import java.util.List;

import com.crossingminds.api.model.UserRating;
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
@JsonPropertyOrder({ "ratings" })
public class UserRatingBulk extends Bulk implements Serializable {

	@JsonProperty("ratings")
	private List<UserRating> ratings;
	private static final long serialVersionUID = 7789202378953572024L;

}
