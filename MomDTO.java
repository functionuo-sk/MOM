package com.mom_management.dto;

import java.time.LocalDateTime;

public class MomDTO {

	private long id;
	private String description;

	private LocalDateTime createdDate;
	private LocalDateTime updatedDate;

	// Constructors, getters, and setters

	public MomDTO() {
	}

	public MomDTO(long id, String description, LocalDateTime createdDate, LocalDateTime updatedDate) {
		super();
		this.id = id;
		this.description = description;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
	}

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
}
