package com.crossingminds.api.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@JsonPropertyOrder({ "id", "name", "rate_limit", "individual_accounts", "service_accounts" })
public class Organization extends Base implements Serializable {

	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("rate_limit")
	private String rateLimit;
	@Builder.Default
	@JsonProperty("individual_accounts")
	private List<IndividualAccount> individualAccounts = null;
	@Builder.Default
	@JsonProperty("service_accounts")
	private List<ServiceAccount> serviceAccounts = null;
	private static final long serialVersionUID = -1753280244684603549L;

}