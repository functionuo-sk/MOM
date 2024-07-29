package com.mom_management.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.UpdateTimestamp;

import com.mom_management.ennum.Status;

@Entity
public class GroupChat {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@ElementCollection(fetch = FetchType.EAGER)
	//@ManyToMany
	private List<Long> members = new ArrayList<>();

	//@Onetomany
	private Long senderId;

	private String message;

	@UpdateTimestamp
	private LocalDateTime date;

	private Status status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getMembers() {
		return members;
	}

	public void setMembers(List<Long> members) {
		this.members = members;
	}

	public Long getSenderId() {
		return senderId;
	}

	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}

	
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	

	public GroupChat() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GroupChat(Long id, String name, List<Long> members, Long senderId, String message, LocalDateTime date,
			Status status) {
		super();
		this.id = id;
		this.name = name;
		this.members = members;
		this.senderId = senderId;
		this.message = message;
		this.date = date;
		this.status = status;
	}

	@Override
	public String toString() {
		return "GroupChat [id=" + id + ", name=" + name + ", members=" + members + ", senderId=" + senderId
				+ ", message=" + message + ", date=" + date + ", status=" + status + "]";
	}


	
	



//		@Id
//		@GeneratedValue(strategy = GenerationType.IDENTITY)
//		private Long id;
	//
//		private String name;
	//
////		@ManyToMany
////		@JoinTable(
////				name = "group_members",
////				joinColumns = @JoinColumn(name = "group_name"),
////				inverseJoinColumns = @JoinColumn(name = "user_id")
////				)
////		private List<UserDao> members;
	//	
	//
//		private List<Long> members = new ArrayList<>();
	//
////		@ManyToOne(fetch = FetchType.EAGER)
////		@JoinColumn(name = "sender_Id")
//		private Long senderId;
	//	
////		private String messages;
	//
//		@UpdateTimestamp
//		private LocalDateTime date;
	//
//		private Status status;
	
}

