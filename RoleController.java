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

import com.mom_management.dto.RoleDTO;
import com.mom_management.exception.ResourceNotFoundException;
import com.mom_management.message.ErrorMessage;
import com.mom_management.message.ResponseMessage;
import com.mom_management.model.Role;
import com.mom_management.service.RoleService;

import javassist.NotFoundException;

@RestController
@RequestMapping("/role")
public class RoleController {

	@Autowired
	private RoleService roleService;

	String message = "";

	@PostMapping
	public ResponseEntity<ResponseMessage> add(@RequestBody RoleDTO roleDto) {
		this.roleService.add(roleDto);
		message = "Role Details successfully saved !!";
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	}

	@PutMapping("/{roleId}")
	public ResponseEntity<ResponseMessage> update(@PathVariable int roleId, @RequestBody RoleDTO roleDto) {
		String message = "";
		roleDto.setRoleId(roleId);
		this.roleService.update(roleDto);
		message = "Role Details successfully Updated !!";
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

	}

	@GetMapping("/{roleId}")
	public Role getById(@PathVariable int roleId) {
	Role roleEntity = roleService.getById(roleId);
	return roleService.getById(roleId);
	}

	@GetMapping("/role")
	public List<Role> getAll() {
	List<Role> roleEntities = roleService.getAll();
	return roleService.getAll();

	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<ResponseMessage> delete(@PathVariable int userId) throws NotFoundException {
		roleService.deleteById(userId);
		message = "Role Details successfully deleted !!";
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));

	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorMessage> handleLocation(ResourceNotFoundException rx) {
		ErrorMessage errorMessage = new ErrorMessage("USER NOT FOUND", rx.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
	}

//	@GetMapping("/active")
//	public List<RoleFilterDto> getActiveUsers() {
//		try {
//			// Log the start of the method
//			logger.info("getActiveUsers method called from controller class");
//
//			List<RoleFilterDto> activeUsers = roleService.getActiveUsers();
//
//			// Log the successful completion of the method
//			logger.info("getActiveUsers method executed successfully. Retrieved {} active users from controller class",
//					activeUsers.size());
//			return roleService.getActiveUsers();
//			// return activeUsers;
//		} catch (Exception e) {
//			// Log the exception and error message
//			logger.error("Error occurred while retrieving active users in controller class", e);
//
//			throw new RuntimeException(e); // Rethrow the exception or handle it according to your needs
//		}
//
//	}

}