package com.mom_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mom_management.model.Message1;
import com.mom_management.model.UserDao;

@Repository
public interface MessageRepository extends JpaRepository<Message1, Long> {

//	List<Message> findBySenderId(Long senderId);
	List<Message1> findBySenderOrReceiver(UserDao sender, UserDao receiver);
}