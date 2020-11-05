package com.crossingminds.api.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.http.Consts;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.TextUtils;

public class StringUtils {

	private StringUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String getEncodedQueryString(Map<String, Object> queryParams) {
		List<BasicNameValuePair> nameValuePairs = queryParams.entrySet().stream().filter(e -> e.getValue() != null)
				.flatMap(e -> (e.getValue() instanceof List)
						? ((List<?>) e.getValue()).stream().map(v -> new BasicNameValuePair(e.getKey(), v + ""))
						: Stream.of(new BasicNameValuePair(e.getKey(), e.getValue() + "")))
				.collect(Collectors.toList());
		var urlParams = URLEncodedUtils.format(nameValuePairs, Consts.UTF_8);
		return !TextUtils.isEmpty(urlParams) ? "?" + urlParams : "";
	}

	@SuppressWarnings("unchecked")
	/**
	 * Should be used only with non-primitive type for parameter
	 * (eg an array like int[] is not applicable because 
	 * autoboxing to Object[] is not possible, instead use Integer[])
	 * 
	 * @param content
	 * @return Object that contains a value of comma separated strings
	 */
	public static Object getCommaSeparatedValue(Object value) {
		String result = null;
		if (value instanceof List) { // If value is a List
			result = ((Collection<Object>) value).stream()
				      .map(String::valueOf)
				      .collect(Collectors.joining(","));
		} else if (value.getClass().isArray()) { // If value is an Array
			result = Arrays.stream((Object[]) value)
	                 .map(String::valueOf)
	                 .collect(Collectors.joining(","));
		} else { // If value is not a List or an Array
			return value;
		}
		return result;
	}

}
