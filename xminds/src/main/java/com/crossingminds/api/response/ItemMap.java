package com.crossingminds.api.response;

import java.io.Serializable;

import com.crossingminds.api.model.Item;
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
public class ItemMap implements Serializable {

	private static final long serialVersionUID = 3825982900617432861L;
	@JsonProperty("item")
	private Item item;

}
