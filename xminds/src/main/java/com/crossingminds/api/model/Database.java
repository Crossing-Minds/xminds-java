package com.crossingminds.api.model;

import java.io.Serializable;

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
@JsonPropertyOrder({ "id", "organization_id", "name", "description", "item_id_type", "user_id_type", "counters" })
public class Database extends Base implements Serializable {

	@JsonProperty("id")
	private String id;
	@JsonProperty("organization_id")
	private String organizationId;
	@JsonProperty("name")
	private String name;
	@JsonProperty("description")
	private String description;
	@JsonProperty("item_id_type")
	private String itemIdType;
	@JsonProperty("user_id_type")
	private String userIdType;
	@JsonProperty("counters")
	private Counters counters;
	private static final long serialVersionUID = 1261106258660845138L;

}
