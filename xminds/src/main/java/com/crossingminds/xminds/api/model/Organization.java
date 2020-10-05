package com.crossingminds.xminds.api.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ "id", "name", "rate_limit", "individual_accounts", "service_accounts" })
public class Organization extends Base implements Serializable {

	@JsonProperty("id")
	private String id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("rate_limit")
	private String rateLimit;
	@JsonProperty("individual_accounts")
	private List<IndividualAccount> individualAccounts = null;
	@JsonProperty("service_accounts")
	private List<ServiceAccount> serviceAccounts = null;
	private final static long serialVersionUID = -1753280244684603549L;

	/**
	 * No args constructor for use in serialization
	 *
	 */
	public Organization() {
	}

	/**
	 *
	 * @param rateLimit
	 * @param serviceAccounts
	 * @param individualAccounts
	 * @param name
	 * @param id
	 */
	public Organization(String id, String name, String rateLimit, List<IndividualAccount> individualAccounts,
			List<ServiceAccount> serviceAccounts) {
		super();
		this.id = id;
		this.name = name;
		this.rateLimit = rateLimit;
		this.individualAccounts = individualAccounts;
		this.serviceAccounts = serviceAccounts;
	}

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("rate_limit")
	public String getRateLimit() {
		return rateLimit;
	}

	@JsonProperty("rate_limit")
	public void setRateLimit(String rateLimit) {
		this.rateLimit = rateLimit;
	}

	@JsonProperty("individual_accounts")
	public List<IndividualAccount> getIndividualAccounts() {
		return individualAccounts;
	}

	@JsonProperty("individual_accounts")
	public void setIndividualAccounts(List<IndividualAccount> individualAccounts) {
		this.individualAccounts = individualAccounts;
	}

	@JsonProperty("service_accounts")
	public List<ServiceAccount> getServiceAccounts() {
		return serviceAccounts;
	}

	@JsonProperty("service_accounts")
	public void setServiceAccounts(List<ServiceAccount> serviceAccounts) {
		this.serviceAccounts = serviceAccounts;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).append("name", name).append("rateLimit", rateLimit)
				.append("individualAccounts", individualAccounts).append("serviceAccounts", serviceAccounts)
				.append("additionalProperties", additionalProperties).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(rateLimit).append(serviceAccounts).append(individualAccounts).append(name)
				.append(id).append(additionalProperties).toHashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (other == this) {
			return true;
		}
		if ((other instanceof Organization) == false) {
			return false;
		}
		Organization rhs = ((Organization) other);
		return new EqualsBuilder().append(rateLimit, rhs.rateLimit).append(serviceAccounts, rhs.serviceAccounts)
				.append(individualAccounts, rhs.individualAccounts).append(name, rhs.name).append(id, rhs.id)
				.append(additionalProperties, rhs.additionalProperties).isEquals();
	}

}