package com.mom_management.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mom_management.ennum.Status;

//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
@Entity
public class Message1 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sender_id")
//	@JsonIgnoreProperties({ "username","firstName","lastName", "active", "email", "token", "department", "role",
//        "created_By", "createdDate", "updated_By", "updated_Date","sender","sentMessages"}) 
	private UserDao sender;
	@ManyToOne
	@JoinColumn(name = "receiver_id")
	@JsonIgnoreProperties({ "email", "username", "firstName", "lastName", "active", "token", "department", "role",
			"created_By", "createdDate", "updated_By", "updated_Date" })
	private UserDao receiver;
	private String content;
	private LocalDateTime timestamp;
	private Status status;

	public UserDao getSender() {
		return sender;
	}

	public void setSender(UserDao sender) {
		this.sender = sender;
	}

	public UserDao getReceiver() {
		return receiver;
	}

	public void setReceiver(UserDao receiver) {
		this.receiver = receiver;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Message1(Long id, UserDao sender, UserDao receiver, String content, LocalDateTime timestamp, Status status) {
		super();
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.content = content;
		this.timestamp = timestamp;
		this.status = status;
	}

	public Message1() {
		super();
		// TODO Auto-generated constructor stub
	}

}
