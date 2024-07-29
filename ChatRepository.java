package com.mom_management.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mom_management.model.Message1;
import com.mom_management.model.Message2;
import com.mom_management.model.UserDao;

@Repository
public interface ChatRepository extends JpaRepository<Message2, Long> {
    // You can define custom query methods here if needed
	
	 List<Message2> findBySenderNameAndReceiverName(String senderName, String receiverName);
}
