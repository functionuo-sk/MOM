package com.mom_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mom_management.model.Mom;

@Repository
public interface MomRepository extends JpaRepository<Mom, Long> {
	// You can add custom queries here if needed
}
