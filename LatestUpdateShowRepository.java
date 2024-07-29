package com.mom_management.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mom_management.model.LatestUpdateShow;
import com.mom_management.model.Task;

@Repository
public interface LatestUpdateShowRepository extends JpaRepository<LatestUpdateShow, Long>{
	
	List<LatestUpdateShow> findAllByTaskOrderByUpdatedDateAsc(Task task);


}
