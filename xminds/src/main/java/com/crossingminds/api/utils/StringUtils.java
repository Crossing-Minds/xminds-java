package com.crossingminds.api.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class StringUtils {

	private StringUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Encode a String to US_ASCII
	 * 
	 * @param value (to encode)
	 * @return String (encoded)
	 */
	public static String encodeUSASCII(String value) {
		try {
			return URLEncoder.encode(value, StandardCharsets.US_ASCII.toString());
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * Decode a String to US_ASCII
	 * 
	 * @param value (to decode)
	 * @return String (decoded)
	 */
	public static String decodeUSASCII(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.US_ASCII.toString());
		} catch (UnsupportedEncodingException e) {
			throw new UnsupportedOperationException(e);
		}
	}

	/**
	 * Encode a pair set of String to US_ASCII
	 * 
	 * @param map (to encode)
	 * @return String (encoded queryString)
	 */
	public static String getEncodedASCIIQueryString(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(String.format("%s=%s", 
				encodeUSASCII(entry.getKey().toString()),
				encodeUSASCII(entry.getValue().toString())));
		}
		return sb.toString();
	}

	/**
	 * Decode a pair set of encoded String (US_ASCII)
	 * 
	 * @param map (to decode)
	 * @return String (decoded queryString)
	 */
	public static String getDecodedASCIIQueryString(Map<?, ?> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (sb.length() > 0) {
				sb.append("&");
			}
			sb.append(String.format("%s=%s", 
				decodeUSASCII(entry.getKey().toString()),
				decodeUSASCII(entry.getValue().toString())));
		}
		return sb.toString();
	}

}

