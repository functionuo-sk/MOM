package com.mom_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mom_management.model.Role;
import com.mom_management.model.UserDao;

@Repository
public interface UserRepository extends JpaRepository<UserDao, Long> {
	UserDao findByEmail(String email);

	UserDao findByToken(String token);

	UserDao findByUsername(String username);

	List<UserDao> findByRole(Role role);

	UserDao findByFirstName(String firstName);
}