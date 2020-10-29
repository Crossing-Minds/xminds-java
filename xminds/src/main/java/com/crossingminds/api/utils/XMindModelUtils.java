package com.crossingminds.api.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crossingminds.api.model.User;

public class XMindModelUtils {

	// Users
	public static Map<String, List<User>> getUsersMapListFromUserList(List<User> users) {
		Map<String, List<User>> usersListMap = new HashMap<>();
		usersListMap.put("users", users);
		return usersListMap;
	}

}
