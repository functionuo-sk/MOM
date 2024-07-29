package com.mom_management.dto;

import java.time.LocalDateTime;

import com.mom_management.ennum.TaskStatus;

public class LatestUpdateShowDTO {

	private long id;
	private TaskStatus status;
	private String remarks;
	private String updatedBy;
	private LocalDateTime updatedDate;
	private long taskId;

	public LatestUpdateShowDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LatestUpdateShowDTO(long id, TaskStatus status, String remarks, String updatedBy, LocalDateTime updatedDate,
			long taskId) {
		super();
		this.id = id;
		this.status = status;
		this.remarks = remarks;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.taskId = taskId;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
