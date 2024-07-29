package com.mom_management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.mom_management.error.ErrorResponse;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(ErrorResponse response) {
		super();
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}
}
