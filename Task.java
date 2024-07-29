package com.mom_management.model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mom_management.ennum.TaskStatus;

@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long taskId;

	private LocalDateTime dateofInitiation;
	private Date dateofTarget;

	// old
//	@OneToOne
//	@JoinColumn(name = "taskOwner")
//	private UserDao taskOwner;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "taskOwner")
	@JsonIgnoreProperties({ "userId", "username", "lastName", "active", "email", "token", "department", "role",
			"created_By", "createdDate", "updated_By", "updated_Date" })
	private UserDao taskOwner;

//	private TaskStatus status;
	@JsonIgnoreProperties
	@Enumerated(EnumType.STRING)
	private TaskStatus status;

	private String remarks;
	@UpdateTimestamp
	private LocalDateTime closingDate;
	private String closingRemarks;
	private String changesInActivity;
	private int extraTimeSpend;

	@Column
	private String createdBy;
	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdDate;
	@Column
	private String updatedBy;
	@Column
	@UpdateTimestamp
	private LocalDateTime updatedDate;

	@OneToOne
	@JoinColumn(name = "momId")
	private Mom mom;

	@ManyToOne
	@JoinColumn(name = "departmentId")
	private Department department;

	@OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
	@JsonIgnoreProperties({ "task" })
	private List<LatestUpdateShow> latestUpdateShows;

//	private long department;

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

	public Mom getMom() {
		return mom;
	}

	public void setMom(Mom mom) {
		this.mom = mom;
	}

	public UserDao getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(UserDao taskOwner) {
		this.taskOwner = taskOwner;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public List<LatestUpdateShow> getLatestUpdateShows() {
		return latestUpdateShows;
	}

	public void setLatestUpdateShows(List<LatestUpdateShow> latestUpdateShows) {
		this.latestUpdateShows = latestUpdateShows;
	}

	public Task() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Task(long taskId, LocalDateTime dateofInitiation, Date dateofTarget, UserDao taskOwner, TaskStatus status,
			String remarks, LocalDateTime closingDate, String closingRemarks, String changesInActivity,
			int extraTimeSpend, String createdBy, LocalDateTime createdDate, String updatedBy,
			LocalDateTime updatedDate, Mom mom, Department department, List<LatestUpdateShow> latestUpdateShows) {
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
		this.mom = mom;
		this.department = department;
		this.latestUpdateShows = latestUpdateShows;
	}

}
