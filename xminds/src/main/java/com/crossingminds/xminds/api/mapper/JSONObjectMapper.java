package com.crossingminds.xminds.api.mapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

public class JSONObjectMapper<T> extends com.fasterxml.jackson.databind.ObjectMapper {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1055755730404981522L;

	/**
	 * Parses the given JSON string into a Map Object
	 * 
	 * @param jsonContent
	 * @return a Map with the content
	 */
	public Map<String, String> jsonToMap(String jsonContent) {
		try {
			return this.readValue(jsonContent, new TypeReference<>() {
			});
		} catch (IOException ioe) {
			throw new CompletionException(ioe);
		}
	}

	/**
	 * Parses the given JSON string into a Java object.
	 * 
	 * @param jsonContent
	 * @param clazz
	 * @return a mapped Java object
	 */
	public T jsonToObject(String jsonContent, Class<T> clazz) {
		try {
			return this.readValue(jsonContent, clazz);
		} catch (IOException ioe) {
			throw new CompletionException(ioe);
		}
	}

	/**
	 * Parses the given JSON string into a Java List object.
	 * 
	 * @param jsonContent
	 * @param typeReference
	 * @return a mapped Java List object
	 */
	public List<T> jsonToObjectList(String jsonContent, TypeReference<T> typeReference) {
		try {
			return (List<T>) this.readValue(jsonContent, typeReference);
		} catch (IOException ioe) {
			throw new CompletionException(ioe);
		}
	}

	/**
	 * Parses the given Java object into a JSON string.
	 * 
	 * @param obj
	 * @return a JSON string value
	 */
	public String objectToJson(Object obj) {
		try {
			return this.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new CompletionException(e);
		}
	}

}