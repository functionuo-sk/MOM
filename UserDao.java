package com.mom_management.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "user")
public class UserDao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;

	@Column(nullable = true)
	private String username;

	@Column
	@JsonIgnore
	private String password;
	@Column
	private String firstName;
	@Column
	private String lastName;
	@Column
	private boolean active;
	@Column
	private String created_By;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	@Column
	private String updated_By;
	@Column
	@UpdateTimestamp
	private LocalDateTime updated_Date;

	@Column(nullable = false, unique = true)
	private String email;

	private String token;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "role_id")
	private Role role;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "departmentId")
	private Department department;

//	@OneToMany(mappedBy = "sender")
//	private List<Message> sentMessages;
//
//	@OneToMany(mappedBy = "receiver")
//	private List<Message> receivedMessages;

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCreated_By() {
		return created_By;
	}

	public void setCreated_By(String created_By) {
		this.created_By = created_By;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdated_By() {
		return updated_By;
	}

	public void setUpdated_By(String updated_By) {
		this.updated_By = updated_By;
	}

	public LocalDateTime getUpdated_Date() {
		return updated_Date;
	}

	public void setUpdated_Date(LocalDateTime updated_Date) {
		this.updated_Date = updated_Date;
	}

//	public List<Message> getSentMessages() {
//		return sentMessages;
//	}
//
//	public void setSentMessages(List<Message> sentMessages) {
//		this.sentMessages = sentMessages;
//	}
//
//	public List<Message> getReceivedMessages() {
//		return receivedMessages;
//	}
//
//	public void setReceivedMessages(List<Message> receivedMessages) {
//		this.receivedMessages = receivedMessages;
//	}

	public UserDao() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserDao(long userId, String username, String password, String firstName, String lastName, boolean active,
			String created_By, LocalDateTime createdDate, String updated_By, LocalDateTime updated_Date, String email,
			String token, Role role,
			Department department/*
									 * , List<Message> sentMessages, List<Message> receivedMessages
									 */) {
		super();
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.active = active;
		this.created_By = created_By;
		this.createdDate = createdDate;
		this.updated_By = updated_By;
		this.updated_Date = updated_Date;
		this.email = email;
		this.token = token;
		this.role = role;
		this.department = department;
//		this.sentMessages = sentMessages;
//		this.receivedMessages = receivedMessages;
	}

}
