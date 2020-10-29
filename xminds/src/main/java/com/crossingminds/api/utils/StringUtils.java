package com.crossingminds.api.utils;

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

}
