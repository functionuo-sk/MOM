package com.mom_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mom_management.model.GroupChat;

public interface GroupRepository  extends JpaRepository<GroupChat, Long>{

	 List<GroupChat> findByName(String name);
	 
	 @Query("SELECT g.message FROM GroupChat g WHERE g.name = :name")
	 List<String> findMessageByGroupName(@Param("name") String name);
	 
	  @Query("SELECT gc FROM GroupChat gc WHERE :members IN elements(gc.members)")
	  List<GroupChat> findByMembers(@Param("members") Long members);
}
