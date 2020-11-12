package com.crossingminds.api.response;

import java.io.Serializable;

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
public class DatabaseStatus implements Serializable {

	@JsonProperty("status")
	private String status;
	private static final long serialVersionUID = -5354276109054465400L;

}
