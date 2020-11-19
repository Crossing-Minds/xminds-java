package com.crossingminds.api.response;

import java.io.Serializable;
import java.util.List;

import com.crossingminds.api.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties("users_m2m")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserList implements Serializable {

	@JsonProperty("users")
	private List<User> users;
	private static final long serialVersionUID = -3239695002786787653L;

}
