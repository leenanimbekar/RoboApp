package com.roboapp.util;

import java.util.Map;
import com.google.common.collect.Maps;

public enum ERROR_CODE {
	
	EMPTY_PRODCUT_NUMBER("Product number cannot be empty"),
	INVALID_WEIGHT("Invalid Weight"),
	INVALID_DISTANCE("Invalid distance value"),
	INTERNAL_ERROR("Internal Server Error"),
	OVERWEIGHT("Overweight !!"),
	EMPTY_FILE("Please Upload File");
	
	private final String text;

	private ERROR_CODE(final String text) {
		this.text = text;
	}

	public String toString() {
		return text;
	}

	public static ERROR_CODE getErrorCode(String str) {
		return errorMap.get(str);
	}

	static final Map<String, ERROR_CODE> errorMap = Maps.newHashMap();

	static {
		for (ERROR_CODE c : ERROR_CODE.values())
			errorMap.put(c.toString(), c);
	}

}
