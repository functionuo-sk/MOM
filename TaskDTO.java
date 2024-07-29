package com.mom_management.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mom_management.ennum.TaskStatus;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDTO {

	private long taskId;
	private LocalDateTime dateofInitiation;
	private Date dateofTarget;
	private String taskOwner;
	private TaskStatus status;
	private String remarks;
	private LocalDateTime closingDate;
	private String closingRemarks;
	private String changesInActivity;
	private int extraTimeSpend;

	private String createdBy;
	private LocalDateTime createdDate;
	private String updatedBy;
	private LocalDateTime updatedDate;

	private long momId;
	private String description;
	private long departmentId;
	
	private List<LatestUpdateShowDTO> latestUpdateShows;


	public TaskDTO() {
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	public long getTaskId() {
		return taskId;
	}

	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public LocalDateTime getDateofInitiation() {
		return dateofInitiation;
	}

	public void setDateofInitiation(LocalDateTime dateofInitiation) {
		this.dateofInitiation = dateofInitiation;
	}

	public Date getDateofTarget() {
		return dateofTarget;
	}

	public void setDateofTarget(Date dateofTarget) {
		this.dateofTarget = dateofTarget;
	}

	public String getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}

	public LocalDateTime getClosingDate() {
		return closingDate;
	}

	public void setClosingDate(LocalDateTime closingDate) {
		this.closingDate = closingDate;
	}

	public String getClosingRemarks() {
		return closingRemarks;
	}

	public void setClosingRemarks(String closingRemarks) {
		this.closingRemarks = closingRemarks;
	}

	public String getChangesInActivity() {
		return changesInActivity;
	}

	public void setChangesInActivity(String changesInActivity) {
		this.changesInActivity = changesInActivity;
	}

	public int getExtraTimeSpend() {
		return extraTimeSpend;
	}

	public void setExtraTimeSpend(int extraTimeSpend) {
		this.extraTimeSpend = extraTimeSpend;
	}

	public long getMomId() {
		return momId;
	}

	public void setMomId(long momId) {
		this.momId = momId;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<LatestUpdateShowDTO> getLatestUpdateShows() {
		return latestUpdateShows;
	}

	public void setLatestUpdateShows(List<LatestUpdateShowDTO> latestUpdateShows) {
		this.latestUpdateShows = latestUpdateShows;
	}

	public TaskDTO(long taskId, LocalDateTime dateofInitiation, Date dateofTarget, String taskOwner, TaskStatus status,
			String remarks, LocalDateTime closingDate, String closingRemarks, String changesInActivity,
			int extraTimeSpend, String createdBy, LocalDateTime createdDate, String updatedBy,
			LocalDateTime updatedDate, long momId, String description, long departmentId,
			List<LatestUpdateShowDTO> latestUpdateShows) {
		super();
		this.taskId = taskId;
		this.dateofInitiation = dateofInitiation;
		this.dateofTarget = dateofTarget;
		this.taskOwner = taskOwner;
		this.status = status;
		this.remarks = remarks;
		this.closingDate = closingDate;
		this.closingRemarks = closingRemarks;
		this.changesInActivity = changesInActivity;
		this.extraTimeSpend = extraTimeSpend;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
		this.momId = momId;
		this.description = description;
		this.departmentId = departmentId;
		this.latestUpdateShows = latestUpdateShows;
	}

	
	
}
