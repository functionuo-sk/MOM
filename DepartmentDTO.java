package com.mom_management.dto;

import java.util.List;

public class DepartmentDTO {

	private long departmentId;
	private String departmentName;

	private List<Long> taskIds; // Assuming this is a list of task IDs associated with the department

	// Default constructor
	public DepartmentDTO() {
	}

	// Constructor with fields
	public DepartmentDTO(Long departmentId, String departmentName, List<Long> taskIds) {
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.taskIds = taskIds;
	}

	// Getter and setter methods
	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public List<Long> getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(List<Long> taskIds) {
		this.taskIds = taskIds;
	}
}
