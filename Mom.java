package com.mom_management.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
public class Mom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String description;

//	@OneToMany(mappedBy = "mom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JoinColumn(name = "taskId")
//    private List<Task> tasks;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_By", referencedColumnName = "userId")
	private UserDao created_By;

	@CreationTimestamp
	private LocalDateTime createdDate;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "updated_By", referencedColumnName = "userId")
	private UserDao updated_By;

	@UpdateTimestamp
	private LocalDateTime updatedDate;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Mom() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Mom(long id, String description, LocalDateTime createdDate, LocalDateTime updatedDate) {
		super();
		this.id = id;
		this.description = description;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

}
