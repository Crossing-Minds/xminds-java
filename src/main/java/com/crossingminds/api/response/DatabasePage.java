package com.crossingminds.api.response;

import java.util.List;

import com.crossingminds.api.model.Database;
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
@JsonPropertyOrder({ "has_next", "next_page", "databases" })
public class DatabasePage extends Page {

	@JsonProperty("databases")
	private List<Database> databases;
	private static final long serialVersionUID = 1375144872677373531L;

}
