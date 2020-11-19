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
public class Item extends HashMap<String, Object> {

	private static final long serialVersionUID = 8765197256116689084L;

	public Object getItemId() {
		return this.get("item_id");
	}

	public void setItemId(Object itemId) {
		this.put("item_id", itemId);
	}

}