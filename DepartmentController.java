package com.mom_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mom_management.dto.DepartmentDTO;
import com.mom_management.exception.ResourceNotFoundException;
import com.mom_management.message.ErrorMessage;
import com.mom_management.message.ResponseMessage;
import com.mom_management.model.Department;
import com.mom_management.service.DepartmentService;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;

	String message = "";

	@PostMapping
	public ResponseEntity<ResponseMessage> add(@RequestBody DepartmentDTO departmentDto) {

		try {

			this.departmentService.add(departmentDto);
			message = "Department Details successfully saved !!";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}

		catch (Exception e) {

			message = "Department details not saved";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}

	}

	@GetMapping("/{departmentId}")
	private Department getById(@PathVariable long departmentId) {
		try {
			return departmentService.getById(departmentId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping
	private List<Department> getAll() {
		try {
			return departmentService.getAll();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@PutMapping("/{departmentId}")
	private ResponseEntity<ResponseMessage> update(@PathVariable long departmentId,
			@RequestBody DepartmentDTO departmentDto) {

		String message = "";

		try {

			departmentDto.setDepartmentId(departmentId);
			this.departmentService.update(departmentDto);
			message = "Department Details successfully Updated !!";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Department details are not updated";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}

	}

//	@GetMapping("/active")
//    public List<DepartmentfilterDto> getActiveUsers() {
//		try
//		{
//		
//        return departmentService.getActiveUsers();
//		} catch (Exception e) {
//		    logger.error("Error fetching active users from controller class", e);
//		    throw new RuntimeException(e);
//		}
//    }

	@DeleteMapping("/{departmentId}")
	private ResponseEntity<ResponseMessage> delete(@PathVariable long departmentId) {
		try {
			departmentService.deleteById(departmentId);
			message = "Department Details successfully deleted !!";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Department details are not deleted" + e.getCause().getMessage();
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}

	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorMessage> handleLocation(ResourceNotFoundException rx) {
		ErrorMessage errorMessage = new ErrorMessage("Department NOT FOUND", rx.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
	}

}
