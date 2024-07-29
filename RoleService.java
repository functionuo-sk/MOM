package com.mom_management.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mom_management.dto.RoleDTO;
import com.mom_management.exception.ResourceNotFoundException;
import com.mom_management.model.Role;
import com.mom_management.repository.RoleRepository;
import com.mom_management.repository.UserRepository;

import javassist.NotFoundException;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	public Role add(RoleDTO roleDto) {

		Role role = new Role();
		role.setRoleId(roleDto.getRoleId());
		role.setRoleName(roleDto.getRoleName());

		return roleRepository.save(role);

	}

	public Role update(RoleDTO roleDto) {

		Role role = new Role();
		role.setRoleId(roleDto.getRoleId());
		role.setRoleName(roleDto.getRoleName());
//		roleRepository.save(role);

		return roleRepository.save(role);

	}

//	public List<RoleFilterDto> getActiveUsers() {
//		try {
//			logger.info("getActiveUsers method called from service class");
//
//			List<RoleEntity> roletEntity = roleRepository.findByStatusOrderByRoleNameAsc(true);
//			List<RoleFilterDto> roleFilterDto = new ArrayList<>();
//
//			for (RoleEntity roleTitle : roletEntity) {
//				RoleFilterDto roleservice = new RoleFilterDto();
//
//				roleservice.setRoleId(roleTitle.getRoleId());
//				roleservice.setRoleName(roleTitle.getRoleName());
//				roleFilterDto.add(roleservice);
//			}
//			logger.info("getActiveUsers method executed successfully in service class. Retrieved {} active users",
//					roleFilterDto.size());
//			return roleFilterDto;
//		} catch (Exception e) {
//			logger.error("Error occurred while retrieving active users service class", e);
//
//			throw new RuntimeException(e);
//		}
//	}

	public Role getById(int roleId) {

		Role roleEntity = roleRepository.findById(roleId)
				.orElseThrow(() -> new ResourceNotFoundException("User Id :" + roleId + "Unavailable"));

		return roleEntity;

	}

	public List<Role> getAll() {

		return roleRepository.findAll();
	}

	public boolean deleteById(int roleId) throws NotFoundException {

		Role roleEntity = roleRepository.findById(roleId).orElseThrow(
				() -> new ResourceNotFoundException("User Id : " + roleId + " is Not Present in DataBase"));
//			roleEntity.setStatus(false); // Update status to false
		roleRepository.save(roleEntity);
		return true;

	}

}