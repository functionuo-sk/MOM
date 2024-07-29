package com.mom_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mom_management.dto.DepartmentDTO;
import com.mom_management.exception.ResourceNotFoundException;
import com.mom_management.model.Department;
import com.mom_management.repository.DepartmentRepository;
import com.mom_management.repository.TaskRepository;

import javassist.NotFoundException;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private TaskRepository taskRepository;

//	public Department add(DepartmentDTO departmentDto) {
//		try {
//			Department departmentEntity = new Department();
//
//			departmentEntity.setDepartmentId(departmentDto.getDepartmentId());
//			departmentEntity.setDepartmentName(departmentDto.getDepartmentName());
////		departmentEntity.setTask(taskRepository.findById(departmentDto.getTaskId()).orElse(null));
//			Task task = taskRepository.findById(departmentDto.getTaskIds()).orElse(null);
//
//			// Add the task to the list of tasks in the department
//			List<Task> tasks = new ArrayList<>();
//			tasks.add(task);
//			departmentEntity.setTasks(tasks);
//
//			return departmentRepository.save(departmentEntity);
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	public Department add(DepartmentDTO departmentDto) {
		try {
			Department departmentEntity = new Department();

			departmentEntity.setDepartmentId(departmentDto.getDepartmentId());
			departmentEntity.setDepartmentName(departmentDto.getDepartmentName());

			// Retrieve tasks from task IDs in DepartmentDTO
//	        List<Task> tasks = new ArrayList<>();
//	        for (Long taskId : departmentDto.getTaskIds()) {
//	            Task task = taskRepository.findById(taskId).orElse(null);
//	            if (task != null) {
//	                tasks.add(task);
//	            } else {
//	                // Handle task not found scenario based on your application logic
//	                // You may choose to throw an exception or log a message
//	            }
//	        }
//	        departmentEntity.setTasks(tasks);

			return departmentRepository.save(departmentEntity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Department getById(long departmentId) {
		try {
			return departmentRepository.findById(departmentId).orElseThrow(
					() -> new ResourceNotFoundException("department Id : " + departmentId + " Unavailable"));
		} catch (Exception e) {

			throw new RuntimeException(e);
		}
	}

	public List<Department> getAll() {
		try {
			return departmentRepository.findAll();
		} catch (Exception e) {

			throw new RuntimeException(e);
		}

	}

//	public Department update(DepartmentDTO departmentDto) {
//	    try {
//	        Department departmentEntity = departmentRepository.findById(departmentDto.getDepartmentId())
//	                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + departmentDto.getDepartmentId()));
//
//	        departmentEntity.setDepartmentName(departmentDto.getDepartmentName());
//	        
//	        // Fetch the Task object from the repository
//	        Task task = taskRepository.findAllById(departmentDto.getTaskIds()).orElse(null);
//	        
//	        // Add the task to the list of tasks in the department
//	        List<Task> tasks = new ArrayList<>();
//	        tasks.add(task);
//	        departmentEntity.setTasks(tasks);
//
//	        return departmentRepository.save(departmentEntity);
//	    } catch (Exception e) {
//	        throw new RuntimeException(e);
//	    }
//	}

	public Department update(DepartmentDTO departmentDto) {
		try {
			// Retrieve the department entity by ID or throw exception if not found
			Department departmentEntity = departmentRepository.findById(departmentDto.getDepartmentId())
					.orElseThrow(() -> new ResourceNotFoundException(
							"Department not found with id: " + departmentDto.getDepartmentId()));

			// Update the department name
			departmentEntity.setDepartmentName(departmentDto.getDepartmentName());

			// Retrieve tasks from task IDs in DepartmentDTO
//	        List<Task> tasks = new ArrayList<>();
//	        for (Long taskId : departmentDto.getTaskIds()) {
//	            Task task = taskRepository.findById(taskId).orElse(null);
//	            if (task != null) {
//	                tasks.add(task);
//	            } else {
//	                // Handle task not found scenario based on your application logic
//	                // You may choose to throw an exception or log a message
//	            }
//	        }
//	        departmentEntity.setTasks(tasks);

			// Save and return the updated department entity
			return departmentRepository.save(departmentEntity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

//	public List<DepartmentfilterDto> getActiveUsers() {
//		try
//		{
//			logger.info("Fetching active users from seervice class");
//		
//		List<DepartmentEntity> departmentEntity = departmentRepository.findByStatusOrderByDepartmentNameAsc(true);
//		List<DepartmentfilterDto> sdepartmentFilterDto = new ArrayList<>();
//
//		for (DepartmentEntity departmentTitle : departmentEntity) {
//			DepartmentfilterDto departmentservice = new DepartmentfilterDto();
//
//			departmentservice.setDepartmentId(departmentTitle.getDepartmentId());
//			departmentservice.setDepartmentName(departmentTitle.getDepartmentName());
//			sdepartmentFilterDto.add(departmentservice);
//		}
//
//		return sdepartmentFilterDto;
//		} catch (Exception e) {
//		    logger.error("Error fetching active users from service class", e);
//		    throw new RuntimeException(e);
//		}
//	}

	public boolean deleteById(long departmentId) throws NotFoundException {
		try {

			Department department = departmentRepository.findById(departmentId)
					.orElseThrow(() -> new ResourceNotFoundException(
							"department Id : " + departmentId + " is Not Present in DataBase"));
			departmentRepository.save(department);
			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

//	public boolean deleteById(long id) {
//		departmentRepository.deleteById(id);
//		return true;
//	}

}
