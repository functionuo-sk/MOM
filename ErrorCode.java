package com.mom_management.error;

public enum ErrorCode {

	UNAUTHORIZED("401", "Unauthorized"), NOT_FOUND("404", "Not Found"), Bad_Request("400", "Bad Request"),
	Internal_Server_Error("500", "Internal Server Error"), Forbidden("403", "Forbidden"),
	Unsupported_Media_Type("", "Unsupported_Media_Type"), Service_Unavailable("", "Service_Unavailable"),
	Success("200", "Success"), Duplicate_Entry("409", "Duplicate_Entry"),
	EMAIL_SHOULD_BE_UNIQUE("1000", "Email id already exist");

	private final String code;
	private final String message;

	ErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
