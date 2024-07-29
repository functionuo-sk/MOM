package com.mom_management.dto;

import java.time.LocalDateTime;

import com.mom_management.ennum.Status;

public class MessageDTO {

	private Long id;
	private Long senderId;
	private Long receiverId;
	private String content;
	private LocalDateTime timestamp;
	private Status status;

	public MessageDTO(Long id, Long senderId, Long receiverId, String content, LocalDateTime timestamp, Status status) {
		super();
		this.id = id;
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.content = content;
		this.timestamp = timestamp;
		this.status = status;
	}

	public MessageDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	public Long getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(Long receiverId) {
		this.receiverId = receiverId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
