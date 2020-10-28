package com.crossingminds.api.response;

import java.util.List;

import com.crossingminds.api.model.IndividualAccount;
import com.crossingminds.api.model.ServiceAccount;
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
public class AccountList {

	@JsonProperty("individual_accounts")
	private List<IndividualAccount> individualAccounts;
	@JsonProperty("service_accounts")
	private List<ServiceAccount> serviceAccounts;

}
