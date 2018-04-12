package com.roboapp.controller;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.roboapp.util.ERROR_CODE;

public class AbstractController {

	private final static Logger logger = Logger.getLogger(AbstractController.class.getName());

	protected Map<String, Object> populateErrors(Exception ex) {
		logger.error("Error", ex);
		HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		if (ex instanceof IllegalArgumentException) {

			httpStatus = HttpStatus.BAD_REQUEST;
		}

		ERROR_CODE ec = ERROR_CODE.getErrorCode(ex.getMessage());
		if (ec == null)
			ec = ERROR_CODE.INTERNAL_ERROR;
		Error error = new Error(ec);
		List<Error> errors = Lists.newArrayList();
		errors.add(error);

		Map<String, Object> toReturn = Maps.newHashMap();
		toReturn.put("errors", errors);
		toReturn.put("status", httpStatus);

		return toReturn;

	}

}

@Data
@AllArgsConstructor
class ApiResponse {

	private Object data;
	private List<Error> errors;
	

}

@Data
@AllArgsConstructor
class Error {

	public Error(ERROR_CODE ec) {
		this.code = ec.name();
		this.message = ec.toString();
	}

	private String code;
	private String message;

}
