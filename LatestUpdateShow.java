package com.mom_management.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.mom_management.ennum.TaskStatus;

@Entity
@Table
public class LatestUpdateShow {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
//	@JsonIgnore
	private Long id;
	private TaskStatus status;
	private String remarks;
	private String updatedBy;
	private LocalDateTime updatedDate;

	@ManyToOne
	private Task task;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public LatestUpdateShow() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LatestUpdateShow(Long id, TaskStatus status, String remarks, String updatedBy, LocalDateTime updatedDate,
			Task task) {
		super();
		this.id = id;
		this.status = status;
		this.remarks = remarks;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.task = task;
	}

}
