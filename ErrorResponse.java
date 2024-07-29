package com.mom_management.error;

public class ErrorResponse {

	private int code;
	private String Message;
	private String status;

	public ErrorResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ErrorResponse(int code, String message, String status) {
		super();
		this.code = code;
		Message = message;
		this.status = status;
	}

}
