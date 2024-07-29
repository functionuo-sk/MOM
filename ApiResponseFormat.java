package com.mom_management.message;

public class ApiResponseFormat {

	private String status;
	private int code;
	private String message;
	private Object data;

	public ApiResponseFormat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ApiResponseFormat(String status, int code, String message, Object data) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
