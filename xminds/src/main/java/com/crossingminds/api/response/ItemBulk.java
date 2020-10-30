package com.crossingminds.api.response;

import java.io.Serializable;
import java.util.List;

import com.crossingminds.api.model.Item;
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
@JsonIgnoreProperties({"items_m2m", "amt"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "items", "has_next" })
public class ItemBulk extends Bulk implements Serializable {

	@JsonProperty("items")
	private List<Item> items;
	@JsonProperty("has_next")
	private boolean hasNext;
	private static final long serialVersionUID = -4738112078560372546L;

}
