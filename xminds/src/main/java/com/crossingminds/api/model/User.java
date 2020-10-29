package com.crossingminds.api.model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class User extends HashMap<String, Object> {

	private static final long serialVersionUID = 2708188142047793763L;

	public Object getUserId() {
		return this.get("user_id");
	}

	public void setUserId(Object userId) {
		this.put("user_id", userId);
	}

}